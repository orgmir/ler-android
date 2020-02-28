package app.luisramos.thecollector.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.luisramos.thecollector.data.SelectAll
import app.luisramos.thecollector.domain.FetchFeedItemsUseCase
import kotlinx.coroutines.launch

class MainViewModel(
    private val fetchFeedsUseCase: FetchFeedItemsUseCase
) : ViewModel() {

    val isLoading = MutableLiveData(false)
    val showError = MutableLiveData<String>()
    val data = MutableLiveData<List<SelectAll>>()

    init {
        loadData()
    }

    fun loadData() = viewModelScope.launch {
        val fetchResult = toggling(isLoading) { fetchFeedsUseCase.fetch() }
        val feeds = fetchResult.getOrElse {
            showError.value = "Failed loading subscriptions"
            emptyList()
        }
        data.value = feeds
    }
}

suspend fun <R> toggling(liveData: MutableLiveData<Boolean>, block: suspend () -> R): R {
    liveData.value = true
    val result = block()
    liveData.value = false
    return result
}