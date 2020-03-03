package app.luisramos.thecollector.domain

import app.luisramos.thecollector.data.download
import app.luisramos.thecollector.parsers.HtmlHeadParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL

class FetchFeedsFromHtmlUseCase {

    suspend fun fetch(urlString: String): Result<List<Pair<String, String>>> {
        Timber.d("Loading page $urlString")

        val url = withContext(Dispatchers.IO) { createURL(urlString) }
            ?: return Result.failure(IOException("Could not create URL for $urlString"))

        val inputStream = withContext(Dispatchers.IO) { url.download() }
            ?: return Result.failure(IOException("Url download is empty for $urlString"))

        return parseHtml(inputStream, url.baseUrl)
    }

    private suspend fun parseHtml(
        inputStream: InputStream,
        baseUrl: String
    ): Result<List<Pair<String, String>>> = withContext(Dispatchers.IO) {
        val pairs = HtmlHeadParser().parse(inputStream, baseUrl)
            .map {
                val (title, link) = it
                title to link
            }.fold(mutableMapOf<String, Pair<String, String>>()) { acc, value ->
                val (_, link) = value
                acc[link] = value
                acc
            }.values.toList()
        Result.success(pairs)
    }

    private fun createURL(urlString: String): URL? =
        try {
            URL(urlString)
        } catch (e: MalformedURLException) {
            if (e.message?.contains("no protocol", ignoreCase = true) == true) {
                try {
                    URL("https://$urlString")
                } catch (e: Exception) {
                    null
                }
            } else {
                null
            }
        }


    private val URL.baseUrl get() = "$protocol://$host"
}