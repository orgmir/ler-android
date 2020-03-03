package app.luisramos.ler.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.luisramos.ler.ParentViewModel
import app.luisramos.ler.data.SelectAll
import app.luisramos.ler.domain.FetchFeedItemsUseCase
import app.luisramos.ler.domain.Preferences
import app.luisramos.ler.domain.SetUnreadFeedItemUseCase
import app.luisramos.ler.ui.event.Event
import app.luisramos.ler.ui.event.postEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(
    val parentViewModel: ParentViewModel,
    private val fetchFeedsUseCase: FetchFeedItemsUseCase,
    private val setUnreadFeedItemUseCase: SetUnreadFeedItemUseCase,
    private val preferences: Preferences
) : ViewModel() {

    val uiState = MutableLiveData<UiState>()

    val goToExternalBrowser = MutableLiveData<Event<String>>()
    val updateListPosition = MutableLiveData<Int>()
    val showReadFeedItems = MutableLiveData(preferences.showReadFeedItems)

    private var fetchJob: Job? = null

    private val observer = Observer<Long> { loadData(it) }

    init {
        parentViewModel.selectedFeed.observeForever(observer)
    }

    override fun onCleared() {
        parentViewModel.selectedFeed.removeObserver(observer)
        super.onCleared()
    }

    fun tappedItem(position: Int) = viewModelScope.launch {
        val data = (uiState.value as? UiState.Success)?.data
        data?.getOrNull(position)?.let {
            setUnreadFeedItemUseCase.setUnread(it.id, false)
            updateListPosition.value = position
            goToExternalBrowser.postEvent(it.link)
        }
    }

    fun toggleUnreadFilter() {
        preferences.showReadFeedItems = !preferences.showReadFeedItems
        showReadFeedItems.value = preferences.showReadFeedItems
        reloadData()
    }

    private fun reloadData() {
        loadData(parentViewModel.selectedFeed.value ?: -1)
    }

    private fun loadData(feedId: Long) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            uiState.value = UiState.Loading
            val showRead = if (preferences.showReadFeedItems) 1L else 0L
            fetchFeedsUseCase.fetch(feedId, showRead).collect { result ->
                uiState.value = result.fold(
                    onFailure = { UiState.Error("Failed loading feeds") },
                    onSuccess = { UiState.Success(it) }
                )
            }
        }
    }

    sealed class UiState {
        object Loading : UiState()
        data class Error(val msg: String) : UiState()
        data class Success(val data: List<SelectAll>) : UiState()
    }
}

suspend fun <R> toggling(liveData: MutableLiveData<Boolean>, block: suspend () -> R): R {
    liveData.value = true
    val result = block()
    liveData.value = false
    return result
}