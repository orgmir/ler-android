package app.luisramos.thecollector.usecases

import com.prof.rssparser.Channel
import com.prof.rssparser.Parser
import timber.log.Timber

class FetchChannelUseCase {
    suspend fun fetchChannel(url: String): Result<Channel> {
        Timber.d("Fetching channel for $url")
        val parser = Parser()
        return try {
            val channel = parser.getChannel(url)
            Timber.d("Got channel ${channel.title}")
            Result.success(channel)
        } catch (e: Exception) {
            Timber.e(e)
            Result.failure(e)
        }
    }
}