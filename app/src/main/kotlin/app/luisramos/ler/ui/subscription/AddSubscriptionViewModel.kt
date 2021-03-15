package app.luisramos.ler.ui.subscription

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.luisramos.ler.R
import app.luisramos.ler.domain.FetchAndSaveChannelUseCase
import app.luisramos.ler.domain.FetchFeedsFromHtmlUseCase
import app.luisramos.ler.domain.fold
import app.luisramos.ler.ui.event.EmptyEvent
import app.luisramos.ler.ui.event.postEmptyEvent
import app.luisramos.ler.ui.subscription.AddSubscriptionViewModel.UiState.*
import kotlinx.coroutines.launch

class AddSubscriptionViewModel(
    private val fetchFeedsFromHtmlUseCase: FetchFeedsFromHtmlUseCase,
    private val fetchAndSaveChannelUseCase: FetchAndSaveChannelUseCase
) : ViewModel() {

    val uiState = MutableLiveData<UiState>()
    val goBack = MutableLiveData<EmptyEvent>()

    fun fetchFeeds(url: String) = viewModelScope.launch {
        uiState.value = Loading
        val resultFeeds = fetchFeedsFromHtmlUseCase.fetch(url)
        uiState.value = resultFeeds.fold(
            onFailure = {
                ShowError(R.string.error_fetching_feeds_add_subscription)
            },
            onSuccess = {
                if (it.isEmpty()) {
                    Empty(R.string.add_subscription_empty_message)
                } else {
                    Loaded(it.map { item -> SubscriptionUiModel(item.first, item.second) })
                }
            }
        )
    }

    fun onItemClicked(position: Int) = viewModelScope.launch {
        val item = (uiState.value as Loaded).items.getOrNull(position) ?: return@launch
        uiState.value = Loading
        val result = fetchAndSaveChannelUseCase.fetchAndSaveChannel(item.url)
        if (result.isFailure) {
            uiState.value = ShowError(R.string.error_adding_subscription)
        } else {
            goBack.postEmptyEvent()
        }
    }

    fun resetState() {
        uiState.value = Loaded(emptyList())
    }


    sealed class UiState {
        object Loading : UiState()
        data class ShowError(val message: Int) : UiState()
        data class Empty(val message: Int) : UiState()
        data class Loaded(val items: List<SubscriptionUiModel>) : UiState()
    }

    data class SubscriptionUiModel(
        val title: String,
        val url: String
    )
}
