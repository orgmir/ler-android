package app.luisramos.thecollector.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.luisramos.thecollector.data.Feed
import app.luisramos.thecollector.usecases.FetchAndSaveChannelUseCase
import app.luisramos.thecollector.usecases.FetchFeedsUseCase
import kotlinx.coroutines.launch


class MainViewModel(
    private val fetchFeedsUseCase: FetchFeedsUseCase,
    private val fetchAndSaveChannelUseCase: FetchAndSaveChannelUseCase
) : ViewModel() {

    val isLoading = MutableLiveData(false)
    val showError = MutableLiveData<String>()
    val data = MutableLiveData<List<Feed>>()

    init {
        loadData()
    }

    fun loadData() = viewModelScope.launch {
        val fetchResult = toggling(isLoading) { fetchFeedsUseCase.fetchFeeds() }
        val feeds = fetchResult.getOrElse {
            showError.value = "Failed loading subscriptions"
            emptyList()
        }
        data.value = feeds
    }

    fun addSubscription(url: String) = viewModelScope.launch {
        // TODO validate url
        // check for http or https
        // TODO add feed auto discovery

        val result = fetchAndSaveChannelUseCase.fetchAndSaveChannel(url)
        if (result.isFailure) {
            showError.value = "Failed adding subscription. Please try again"
        } else {
            loadData()
        }
    }
}

suspend fun <R> toggling(liveData: MutableLiveData<Boolean>, block: suspend () -> R): R {
    liveData.value = true
    val result = block()
    liveData.value = false
    return result
}