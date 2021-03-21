package app.luisramos.ler.ui.feeds

import androidx.lifecycle.*
import app.luisramos.ler.data.SelectAll
import app.luisramos.ler.domain.*
import app.luisramos.ler.ui.ScaffoldViewModel
import app.luisramos.ler.ui.event.EmptyEvent
import app.luisramos.ler.ui.event.Event
import app.luisramos.ler.ui.event.postEmptyEvent
import app.luisramos.ler.ui.event.postEvent
import app.luisramos.ler.ui.navigation.Navigation
import app.luisramos.ler.ui.views.UiState
import app.luisramos.ler.ui.views.data
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FeedItemsListViewModel(
    private val parentViewModel: ScaffoldViewModel,
    private val fetchFeedsUseCase: FetchFeedItemsUseCase,
    private val setUnreadFeedItemUseCase: SetUnreadFeedItemUseCase,
    private val deleteFeedUseCase: DeleteFeedUseCase,
    private val toggleNotifyMeFeedUseCase: ToggleNotifyMeFeedUseCase,
    private val navigation: Navigation,
    private val preferences: Preferences
) : ViewModel() {

    val uiState = MutableLiveData<UiState<SelectAll>>()

    val updateListPosition = MutableLiveData<Int>()
    val showReadFeedItems = MutableLiveData(preferences.hideReadFeedItems)
    val isNotifyMenuOptionVisible = parentViewModel.selectedFeed.map { it != -1L }
    val isNotifyMenuOptionChecked = MutableLiveData(false)
    val isDeleteMenuOptionVisible = isNotifyMenuOptionVisible
    val shouldUpdateMenuOptions = MediatorLiveData<EmptyEvent>().apply {
        addSource(showReadFeedItems) {
            postEmptyEvent()
        }
        addSource(isNotifyMenuOptionChecked) {
            postEmptyEvent()
        }
        addSource(isNotifyMenuOptionVisible) {
            postEmptyEvent()
        }
    }
    val showDeleteConfirmation = MutableLiveData<Event<String>>()

    private var fetchJob: Job? = null

    init {
        parentViewModel.selectedFeed.observeForever(::loadData)
    }

    override fun onCleared() {
        parentViewModel.selectedFeed.removeObserver(::loadData)
        super.onCleared()
    }

    fun tappedItem(position: Int) = viewModelScope.launch {
        getItem(position)?.let {
            setUnreadFeedItemUseCase.setUnread(it.id, false)
            navigation.goToExternalBrowser(it.link)
        }
    }

    fun toggleUnreadFilter() {
        preferences.hideReadFeedItems = !preferences.hideReadFeedItems
        showReadFeedItems.value = preferences.hideReadFeedItems
        loadData()
    }

    fun markAllAsRead() = viewModelScope.launch {
        parentViewModel.selectedFeed.value?.let {
            setUnreadFeedItemUseCase.setUnreadForFeedId(it, false)
        }
    }

    fun toggleUnread(position: Int) = viewModelScope.launch {
        getItem(position)?.let {
            setUnreadFeedItemUseCase.setUnread(it.id, it.unread?.not() ?: false)
        }
    }

    fun toggleNotifyMe() {
        val title = parentViewModel.title.value ?: return
        if (title == "All") return

        viewModelScope.launch {
            val id = parentViewModel.selectedFeed.value
            id?.let { toggleNotifyMeFeedUseCase.toggleNotifyMe(id) }
        }
    }

    fun tapDeleteFeed() {
        val title = parentViewModel.title.value ?: return
        if (title == "All") return

        showDeleteConfirmation.postEvent(title)
    }

    fun deleteSelectedFeed() = viewModelScope.launch {
        val id = parentViewModel.selectedFeed.value
        id?.let { deleteFeedUseCase.deleteFeed(id) }
        parentViewModel.selectedFeed.value = -1 // reset to "All" filter
    }

    fun onSettingsMenuClicked() {
        navigation.goToSettingsScreen()
    }

    fun loadData() {
        loadData(parentViewModel.selectedFeed.value ?: -1)
    }

    private fun loadData(feedId: Long) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            uiState.value = UiState.Loading
            val showRead = if (preferences.hideReadFeedItems) 0L else 1L
            fetchFeedsUseCase.fetch(feedId, showRead).collect { result ->
                updateNotifyMeMenu(result)
                uiState.value = result.fold(
                    onFailure = { UiState.Error("Failed loading feeds") },
                    onSuccess = { UiState.Success(it) }
                )
            }
        }
    }

    private fun updateNotifyMeMenu(result: Result<List<SelectAll>>) {
        isNotifyMenuOptionChecked.value = result.getOrNull()?.let {
            it.firstOrNull()?.run { feedId != -1L && feedNotify ?: false }
        } ?: false
    }

    private fun getItem(position: Int): SelectAll? = uiState.value?.data?.getOrNull(position)
}