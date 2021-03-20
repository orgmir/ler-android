package app.luisramos.ler.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.luisramos.ler.R
import app.luisramos.ler.domain.FetchFeedUseCase
import app.luisramos.ler.ui.feeds.FeedListScreen
import app.luisramos.ler.ui.screen.Screen
import kotlinx.coroutines.launch

class ScaffoldViewModel(
    private val fetchFeedUseCase: FetchFeedUseCase
) : ViewModel() {
    val selectedFeed = MutableLiveData<Long>(-1)
    val title = MutableLiveData("All")
    val navigationIcon = MutableLiveData(R.drawable.ic_menu)

    private val feedObserver = Observer<Long> { updateTitle(it) }

    init {
        selectedFeed.observeForever(feedObserver)
    }

    override fun onCleared() {
        selectedFeed.removeObserver(feedObserver)
        super.onCleared()
    }

    fun onNewScreen(screen: Screen) {
        when (screen) {
            is FeedListScreen -> {
                updateTitle(selectedFeed.value ?: -1L)
                navigationIcon.value = R.drawable.ic_menu
            }
            else -> {
                // title will be set by screen view models
                navigationIcon.value = R.drawable.ic_arrow_back
            }
        }
    }

    private fun updateTitle(id: Long) = viewModelScope.launch {
        title.value = when (id) {
            -1L -> "All"
            else -> fetchFeedUseCase.fetch(id)?.title ?: "Items"
        }
    }
}