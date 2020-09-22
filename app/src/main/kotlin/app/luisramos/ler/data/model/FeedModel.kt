package app.luisramos.ler.data.model

import app.luisramos.ler.domain.parsers.AtomXmlParser
import app.luisramos.ler.domain.parsers.RssXmlParser
import java.text.SimpleDateFormat
import java.util.*

data class FeedModel(
    val title: String,
    val link: String,
    val feedLink: String = "",
    val description: String? = null,
    val updated: Date,
    val items: List<FeedItemModel> = listOf()
)

val atomFeedDateFormatter1 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
val atomFeedDateFormatter2 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH)
val rssFeedDateFormatter = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)

fun parseAtomDateString(dateString: String): Date? = when {
    dateString.contains("Z", ignoreCase = true) -> atomFeedDateFormatter1.parse(dateString)
    else -> atomFeedDateFormatter2.parse(dateString)
}

fun RssXmlParser.Channel.toFeed() = FeedModel(
    title = title ?: "",
    link = link ?: "",
    description = description,
    updated = lastBuildDate?.let { rssFeedDateFormatter.parse(it) } ?: Date(),
    items = items.map { it.toFeedItem() }
)

fun AtomXmlParser.Feed.toFeed() = FeedModel(
    title = title ?: "",
    link = link ?: "",
    description = subtitle,
    updated = updated?.let { parseAtomDateString(it) } ?: Date(),
    items = entries.map { it.toFeedItem() }
)

data class FeedItemModel(
    val id: String,
    val title: String,
    val description: String?,
    val link: String,
    val published: Date,
    val updated: Date?
)

fun RssXmlParser.Item.toFeedItem() = FeedItemModel(
    id = guid ?: "",
    title = title ?: "",
    description = description,
    link = link ?: "",
    published = pubDate?.let { rssFeedDateFormatter.parse(it) } ?: Date(),
    updated = null
)

fun AtomXmlParser.Entry.toFeedItem() = FeedItemModel(
    id = id ?: "",
    title = title ?: "",
    description = summary,
    link = link ?: "",
    published = published?.let { parseAtomDateString(it) } ?: Date(),
    updated = updated?.let { parseAtomDateString(it) } ?: Date()
)