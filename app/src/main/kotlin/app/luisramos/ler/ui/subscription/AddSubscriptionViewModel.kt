package app.luisramos.ler.ui.subscription

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.luisramos.ler.domain.FetchAndSaveChannelUseCase
import app.luisramos.ler.domain.FetchFeedsFromHtmlUseCase
import app.luisramos.ler.domain.fold
import app.luisramos.ler.ui.event.EmptyEvent
import app.luisramos.ler.ui.event.postEmptyEvent
import kotlinx.coroutines.launch

class AddSubscriptionViewModel(
    private val fetchFeedsFromHtmlUseCase: FetchFeedsFromHtmlUseCase,
    private val fetchAndSaveChannelUseCase: FetchAndSaveChannelUseCase
) : ViewModel() {

    val uiState = MutableLiveData<UiState>()
    val goBack = MutableLiveData<EmptyEvent>()

    fun fetchFeeds(url: String) = viewModelScope.launch {
        uiState.value = UiState.Loading
        val resultFeeds = fetchFeedsFromHtmlUseCase.fetch(url)
        uiState.value = resultFeeds.fold(
            onFailure = {
                UiState.ShowError("Error loading pages for this url.")
            },
            onSuccess = {
                UiState.Loaded(it)
            }
        )
    }

    fun addSubscription(url: String) = viewModelScope.launch {
        uiState.value = UiState.Loading
        val result = fetchAndSaveChannelUseCase.fetchAndSaveChannel(url)
        if (result.isFailure) {
            uiState.value = UiState.ShowError("Failed adding subscription. Please try again")
        } else {
            goBack.postEmptyEvent()
        }
    }

    sealed class UiState {
        object Loading : UiState()
        data class ShowError(val errorMsg: String) : UiState()
        data class Loaded(val items: List<Pair<String, String>>) : UiState()
    }
}
