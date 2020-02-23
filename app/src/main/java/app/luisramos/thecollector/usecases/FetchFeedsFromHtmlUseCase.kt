package app.luisramos.thecollector.usecases

import app.luisramos.thecollector.parsers.HtmlHeadParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class FetchFeedsFromHtmlUseCase {

    suspend fun fetch(urlString: String): Result<List<Pair<String, String>>> {
        Timber.d("Loading page $urlString")

        val url = withContext(Dispatchers.IO) { URL(urlString) }

        val inputStreamResult = downloadPage(url)
        val inputStream = inputStreamResult.getOrElse { return Result.failure(it) }

        return parseHtml(inputStream, url.baseUrl)
    }

    private suspend fun parseHtml(
        inputStream: InputStream,
        baseUrl: String
    ): Result<List<Pair<String, String>>> = withContext(Dispatchers.IO) {
        try {
            val pairs = HtmlHeadParser().parse(inputStream, baseUrl)
                .map {
                    val (title, link) = it
                    title to link
                }
            Result.success(pairs)
        } catch (e: IOException) {
            Result.failure<List<Pair<String, String>>>(e)
        }
    }

    private suspend fun downloadPage(url: URL): Result<InputStream> =
        withContext(Dispatchers.IO) {
            try {
                val inputStream = downloadUrl(url)
                if (inputStream != null) {
                    Result.success(inputStream)
                } else {
                    Result.failure(IOException("Empty document for $url"))
                }
            } catch (e: IOException) {
                Result.failure<InputStream>(e)
            }
        }


    @Throws(IOException::class)
    private fun downloadUrl(url: URL): InputStream? {
        return (url.openConnection() as? HttpURLConnection)?.run {
            readTimeout = 10000
            connectTimeout = 15000
            requestMethod = "GET"
            doInput = true
            connect()
            inputStream
        }
    }

    private val URL.baseUrl get() = "$protocol://$host"
}