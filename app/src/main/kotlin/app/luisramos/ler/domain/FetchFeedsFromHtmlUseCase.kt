package app.luisramos.ler.domain

import app.luisramos.ler.data.download
import app.luisramos.ler.domain.parsers.HtmlHeadParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL
import kotlin.coroutines.CoroutineContext

interface FetchFeedsFromHtmlUseCase {
    suspend fun fetch(urlString: String): Result<List<Pair<String, String>>>
}

class DefaultFetchFeedsFromHtmlUseCase(
    private val coroutineContext: CoroutineContext = Dispatchers.IO
) : FetchFeedsFromHtmlUseCase {

    override suspend fun fetch(urlString: String): Result<List<Pair<String, String>>> =
        withContext(coroutineContext) {
            Timber.d("Loading page $urlString")

            val url = createURL(urlString)
                ?: return@withContext Result.failure(IOException("Could not create URL for $urlString"))
            url.download {
                val feeds = parseHtml(it, url.baseUrl)
                Result.success(feeds)
            }
                ?: Result.failure(IOException("Url download is empty for $urlString"))
        }

    private fun parseHtml(inputStream: InputStream, baseUrl: String): List<Pair<String, String>> =
        HtmlHeadParser().parse(inputStream, baseUrl)
            .map {
                val (title, link) = it
                title to link
            }.fold(mutableMapOf<String, Pair<String, String>>()) { acc, value ->
                val (_, link) = value
                acc[link] = value
                acc
            }.values.toList()

    private fun createURL(urlString: String): URL? =
        try {
            URL(urlString)
        } catch (e: MalformedURLException) {
            if (e.message?.contains("no protocol", ignoreCase = true) == true) {
                try {
                    URL("http://$urlString")
                } catch (e: Exception) {
                    null
                }
            } else {
                null
            }
        }


    private val URL.baseUrl get() = "$protocol://$host"
}