package app.luisramos.thecollector.usecases

import app.luisramos.thecollector.data.model.FeedModel
import app.luisramos.thecollector.parsers.RssAtomCombinedParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import java.net.URL

class FetchChannelUseCase {
    suspend fun fetchChannel(urlString: String): Result<FeedModel> = withContext(Dispatchers.IO) {
        Timber.d("Fetching channel for $urlString")
        val parser = RssAtomCombinedParser()
        try {
            val url = URL(urlString)
            val inputStream = url.download()
                ?: return@withContext Result.failure<FeedModel>(IOException("Download failed for $urlString"))
            val feed = parser.parse(inputStream)
                ?: return@withContext Result.failure<FeedModel>(IOException("Feed parsing failed for $urlString"))

            Timber.d("Got feed ${feed.title}")
            Result.success(feed)
        } catch (e: Exception) {
            Timber.e(e)
            Result.failure<FeedModel>(e)
        }
    }
}