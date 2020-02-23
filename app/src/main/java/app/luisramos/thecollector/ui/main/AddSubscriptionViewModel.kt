package app.luisramos.thecollector.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.luisramos.thecollector.usecases.FetchAndSaveChannelUseCase
import app.luisramos.thecollector.usecases.FetchFeedsFromHtmlUseCase
import kotlinx.coroutines.launch

class AddSubscriptionViewModel(
    private val fetchFeedsFromHtmlUseCase: FetchFeedsFromHtmlUseCase,
    private val fetchAndSaveChannelUseCase: FetchAndSaveChannelUseCase
) : ViewModel() {

    val uiState = MutableLiveData<UiState>()

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
        // TODO validate url
        //  check for http or https
        // TODO add feed auto discovery

        val result = fetchAndSaveChannelUseCase.fetchAndSaveChannel(url)
        if (result.isFailure) {
            uiState.value =
                UiState.ShowError("Failed adding subscription. Please try again")
        } else {
            // go back to previous fragment
        }
    }

    sealed class UiState {
        object Loading : UiState()
        data class ShowError(val errorMsg: String) : UiState()
        data class Loaded(val items: List<Pair<String, String>>) : UiState()
    }
}

