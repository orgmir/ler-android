package app.luisramos.thecollector.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.luisramos.thecollector.ParentViewModel
import app.luisramos.thecollector.data.SelectAll
import app.luisramos.thecollector.domain.FetchFeedItemsUseCase
import app.luisramos.thecollector.domain.SetUnreadFeedItemUseCase
import app.luisramos.thecollector.ui.event.Event
import app.luisramos.thecollector.ui.event.postEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(
    val parentViewModel: ParentViewModel,
    private val fetchFeedsUseCase: FetchFeedItemsUseCase,
    private val setUnreadFeedItemUseCase: SetUnreadFeedItemUseCase
) : ViewModel() {

    val uiState = MutableLiveData<UiState>()

    val goToExternalBrowser = MutableLiveData<Event<String>>()
    val updateListPosition = MutableLiveData<Int>()

    private var fetchJob: Job? = null

    private val observer = Observer<Long> { loadData(it) }

    init {
        parentViewModel.selectedFeed.observeForever(observer)
    }

    override fun onCleared() {
        parentViewModel.selectedFeed.removeObserver(observer)
        super.onCleared()
    }

    private fun loadData(feedId: Long) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            uiState.value = UiState.Loading
            fetchFeedsUseCase.fetch(feedId).collect { result ->
                uiState.value = result.fold(
                    onFailure = { UiState.Error("Failed loading feeds") },
                    onSuccess = { UiState.Success(it) }
                )
            }
        }
    }

    fun tappedItem(position: Int) = viewModelScope.launch {
        val data = (uiState.value as? UiState.Success)?.data
        data?.getOrNull(position)?.let {
            setUnreadFeedItemUseCase.setUnread(it.id, false)
            updateListPosition.value = position
            goToExternalBrowser.postEvent(it.link)
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