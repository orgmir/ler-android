package app.luisramos.thecollector.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.luisramos.thecollector.data.SelectAll
import app.luisramos.thecollector.domain.FetchFeedItemsUseCase
import app.luisramos.thecollector.domain.SetUnreadFeedItemUseCase
import app.luisramos.thecollector.ui.event.Event
import app.luisramos.thecollector.ui.event.postEvent
import kotlinx.coroutines.launch

class MainViewModel(
    private val fetchFeedsUseCase: FetchFeedItemsUseCase,
    private val setUnreadFeedItemUseCase: SetUnreadFeedItemUseCase
) : ViewModel() {

    val uiState = MutableLiveData<UiState>()

    val goToExternalBrowser = MutableLiveData<Event<String>>()
    val updateListPosition = MutableLiveData<Int>()

    init {
        loadData()
    }

    fun loadData() = viewModelScope.launch {
        uiState.value = UiState.Loading
        val fetchResult = fetchFeedsUseCase.fetch()
        uiState.value = fetchResult.fold(
            onFailure = { UiState.Error("Failed loading feeds") },
            onSuccess = { UiState.Success(it) }
        )
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