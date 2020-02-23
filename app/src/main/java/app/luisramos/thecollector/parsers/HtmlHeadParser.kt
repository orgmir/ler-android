package app.luisramos.thecollector.parsers

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.io.InputStream

class HtmlHeadParser {

    @Throws(IOException::class)
    fun parse(inputStream: InputStream, baseUri: String): List<FeedLink> {
        inputStream.use {
            val document = Jsoup.parse(inputStream, null, baseUri)
            return readDocument(document)
        }
    }

    private fun readDocument(document: Document): List<FeedLink> =
        document.select("link")
            .filter {
                val type = it.attr("type")
                type == "application/atom+xml" || type == "application/rss+xml"
            }
            .map {
                val title = it.attr("title")
                val link = it.attr("href")
                FeedLink(title, link)
            }

    data class FeedLink(
        val title: String,
        val link: String
    )
}