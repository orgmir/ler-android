package app.luisramos.ler.domain

import app.luisramos.ler.data.download
import app.luisramos.ler.data.model.FeedModel
import app.luisramos.ler.parsers.RssAtomCombinedParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import java.net.URL

class FetchChannelUseCase {
    suspend fun fetchChannel(urlString: String): Result<FeedModel> {
        Timber.d("Fetching channel for $urlString")
        val parser = RssAtomCombinedParser()
        return try {
            val url = URL(urlString)
            val inputStream = withContext(Dispatchers.IO) { url.download() }
                ?: return Result.failure(IOException("Download failed for $urlString"))
            val feed = withContext(Dispatchers.IO) { parser.parse(inputStream) }
                ?: return Result.failure(IOException("Feed parsing failed for $urlString"))

            Timber.d("Got feed ${feed.title}")
            Result.success(feed.copy(feedLink = urlString))
        } catch (e: Exception) {
            Timber.e(e)
            Result.failure(e)
        }
    }
}