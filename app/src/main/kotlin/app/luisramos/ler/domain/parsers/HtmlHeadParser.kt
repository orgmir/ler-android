package app.luisramos.ler.domain.parsers

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.InputStream

class HtmlHeadParser {

    fun parse(inputStream: InputStream, baseUri: String): List<FeedLink> =
        inputStream.use {
            try {
                val document = Jsoup.parse(inputStream, null, baseUri)
                readDocument(document, baseUri)
            } catch (e: Exception) {
                emptyList()
            }
        }

    private fun readDocument(document: Document, baseUri: String): List<FeedLink> =
        document.select("link")
            .filter {
                val type = it.attr("type")
                type == "application/atom+xml" || type == "application/rss+xml"
            }
            .map {
                val title = it.attr("title")
                var link = it.attr("href")
                if (!link.contains("http")) {
                    link = "$baseUri$link"
                }
                FeedLink(title, link)
            }

    data class FeedLink(
        val title: String,
        val link: String
    )
}