package app.luisramos.ler

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.luisramos.ler.domain.FetchFeedUseCase
import kotlinx.coroutines.launch

class ParentViewModel(
    private val fetchFeedUseCase: FetchFeedUseCase
) : ViewModel() {
    val selectedFeed = MutableLiveData<Long>(-1)
    val title = MutableLiveData("All")

    private val feedObserver = Observer<Long> { updateTitle(it) }

    init {
        selectedFeed.observeForever(feedObserver)
    }

    override fun onCleared() {
        selectedFeed.removeObserver(feedObserver)
        super.onCleared()
    }

    private fun updateTitle(id: Long) = viewModelScope.launch {
        title.value = when (id) {
            -1L -> "All"
            else -> fetchFeedUseCase.fetch(id)?.title ?: "Items"
        }
    }
}