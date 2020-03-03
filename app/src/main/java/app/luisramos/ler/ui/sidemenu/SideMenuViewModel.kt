package app.luisramos.ler.ui.sidemenu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.luisramos.ler.ParentViewModel
import app.luisramos.ler.data.FeedsWithCount
import app.luisramos.ler.domain.FetchFeedsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SideMenuViewModel(
    parentViewModel: ParentViewModel,
    private val fetchFeedsUseCase: FetchFeedsUseCase
) : ViewModel() {

    val uiState = MutableLiveData<UiState>()
    val selectedFeed = parentViewModel.selectedFeed

    private var fetchJob: Job? = null

    init {
        loadData()
    }

    fun loadData() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
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
        val item = (uiState.value as? UiState.Success)?.items?.getOrNull(position)
        item?.let {
            selectedFeed.value = item.id
        }
    }

    private fun List<FeedsWithCount>.toSideMenuItems() = map {
        SideMenuItem(it.id, it.title, it.itemsCount.toString())
    }

    sealed class UiState {
        object Loading : UiState()
        data class Error(val msg: String) : UiState()
        data class Success(val items: List<SideMenuItem>) : UiState()
    }

    data class SideMenuItem(
        val id: Long,
        val title: String,
        val count: String
    )
}