package app.luisramos.ler.domain

import app.luisramos.ler.data.download
import app.luisramos.ler.data.model.FeedModel
import app.luisramos.ler.domain.parsers.FeedParser
import app.luisramos.ler.domain.parsers.RssAtomCombinedParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import java.net.URL
import java.util.*
import kotlin.coroutines.CoroutineContext

interface FetchChannelUseCase {
    suspend fun fetchChannel(urlString: String): Result<FeedModel>
}

class DefaultFetchChannelUseCase(
    private val parser: FeedParser = RssAtomCombinedParser(),
    private val coroutineContext: CoroutineContext = Dispatchers.IO
) : FetchChannelUseCase {
    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun fetchChannel(urlString: String): Result<FeedModel> =
        withContext(coroutineContext) {
            try {
                Timber.d("Fetching channel for $urlString")

                val url = URL(urlString)
                url.download {
                    val feed = parser.parse(it)
                        ?: return@download Result.failure(IOException("Feed parsing failed for $urlString"))
                    Timber.d("Got feed ${feed.title}")
                    Result.success(feed.copy(feedLink = urlString))
                }
                    ?: Result.failure(IOException("Download failed for $urlString"))
            } catch (e: Exception) {
                Timber.e(e)
                Result.failure(e)
            }
        }
}

class FakeFetchChannelUseCase : FetchChannelUseCase {
    var mockResult: Result<FeedModel> = Result.success(
        FeedModel("title", "http://example.com", updated = Date())
    )

    override suspend fun fetchChannel(urlString: String): Result<FeedModel> = mockResult
}