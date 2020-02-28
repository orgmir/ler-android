package app.luisramos.thecollector.ui.sidemenu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.luisramos.thecollector.data.Feed
import app.luisramos.thecollector.domain.FetchFeedsUseCase
import kotlinx.coroutines.launch

class SideMenuViewModel(
    private val fetchFeedsUseCase: FetchFeedsUseCase
) : ViewModel() {

    val uiState = MutableLiveData<UiState>()

    init {
        loadData()
    }

    fun loadData() = viewModelScope.launch {
        uiState.value = UiState.Loading
        val fetchResult = fetchFeedsUseCase.fetchFeeds()
        uiState.value = fetchResult.fold(
            onFailure = { UiState.Error("Failed loading subscriptions") },
            onSuccess = { UiState.Success(it) }
        )
    }

    sealed class UiState {
        object Loading : UiState()
        data class Error(val msg: String) : UiState()
        data class Success(val items: List<Feed>) : UiState()
    }
}