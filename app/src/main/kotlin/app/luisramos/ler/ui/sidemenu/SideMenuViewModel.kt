package app.luisramos.ler.ui.sidemenu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.luisramos.ler.data.FeedsWithCount
import app.luisramos.ler.domain.FetchFeedsUseCase
import app.luisramos.ler.domain.fold
import app.luisramos.ler.ui.ScaffoldViewModel
import app.luisramos.ler.ui.navigation.Navigation
import app.luisramos.ler.ui.views.UiState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SideMenuViewModel(
    parentViewModel: ScaffoldViewModel,
    private val fetchFeedsUseCase: FetchFeedsUseCase,
    private val navigation: Navigation
) : ViewModel() {

    val uiState = MutableLiveData<UiState<SideMenuItem>>()
    private val selectedFeed = parentViewModel.selectedFeed

    init {
        viewModelScope.launch {
            uiState.value = UiState.Loading
            fetchFeedsUseCase.fetchFeeds().collect { result ->
                uiState.value = result.fold(
                    onFailure = { UiState.Error("Failed loading subscriptions") },
                    onSuccess = { UiState.Success(it.toSideMenuItems()) }
                )
            }
        }
    }

    fun onItemTapped(position: Int) {
        val state = uiState.value as? UiState.Success ?: return
        val item = state.data.getOrNull(position)
        item?.let {
            selectedFeed.value = item.id
            uiState.value = state.copy(
                data = state.data.map {
                    it.copy(isSelected = it.id == item.id)
                }
            )
        }
    }

    private fun List<FeedsWithCount>.toSideMenuItems() = map { item ->

        SideMenuItem(
            id = item.id,
            title = item.title,
            count = when (item.itemsCount) {
                null -> ""
                0.0 -> ""
                else -> String.format("%d", item.itemsCount.toInt())
            },
            isSelected = (selectedFeed.value ?: -1L) == item.id
        )
    }

    fun addSubscriptionButtonClicked() {
        navigation.goToAddSubscriptionScreen()
    }

    data class SideMenuItem(
        val id: Long,
        val title: String,
        val count: String,
        val isSelected: Boolean = false
    )
}