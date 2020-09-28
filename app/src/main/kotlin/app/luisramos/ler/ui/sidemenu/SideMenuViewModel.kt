package app.luisramos.ler.ui.sidemenu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.luisramos.ler.data.FeedsWithCount
import app.luisramos.ler.domain.FetchFeedsUseCase
import app.luisramos.ler.ui.ScaffoldViewModel
import app.luisramos.ler.ui.views.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SideMenuViewModel(
    parentViewModel: ScaffoldViewModel,
    private val fetchFeedsUseCase: FetchFeedsUseCase
) : ViewModel() {

    val uiState = MutableLiveData<UiState<SideMenuItem>>()
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
            count = item.itemsCount.toString().let { if (it == "0" || it == "null") "" else it },
            isSelected = (selectedFeed.value ?: -1L) == item.id
        )
    }

    data class SideMenuItem(
        val id: Long,
        val title: String,
        val count: String,
        val isSelected: Boolean = false
    )
}