package app.luisramos.thecollector.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prof.rssparser.Parser
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel : ViewModel() {
    fun addSubscription(url: String) = viewModelScope.launch {
        // TODO validate url
        Timber.d("Fetching channel for $url")
        val parser = Parser()
        try {
            val channel = parser.getChannel(url)
            Timber.d("Got channel ${channel.title}")
            Timber.d(channel.articles.toString())
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}
