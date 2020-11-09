package app.luisramos.ler.data.model

import app.luisramos.ler.domain.parsers.Entry
import app.luisramos.ler.domain.parsers.Feed
import app.luisramos.ler.domain.parsers.RssXmlParser
import timber.log.Timber
import java.text.ParseException
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

fun parseAtomDate(dateString: String?): Date? {
    dateString ?: return null
    return try {
        when {
            dateString.contains("Z", ignoreCase = true) -> atomFeedDateFormatter1.parse(dateString)
            else -> atomFeedDateFormatter2.parse(dateString)
        }
    } catch (e: ParseException) {
        Timber.e(e)
        null
    }
}

fun RssXmlParser.Channel.toFeedModel() = FeedModel(
    title = title ?: "",
    link = link ?: "",
    description = description,
    updated = lastBuildDate?.let { rssFeedDateFormatter.parse(it) } ?: Date(),
    items = items.map { it.toFeedItem() }
)

fun Feed.toFeedModel() = FeedModel(
    title = title ?: "",
    link = link ?: "",
    description = subtitle,
    updated = parseAtomDate(updated) ?: Date(),
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

fun Entry.toFeedItem() = FeedItemModel(
    id = id ?: "",
    title = title ?: "",
    description = summary,
    link = link ?: "",
    published = parseAtomDate(published) ?: Date(),
    updated = parseAtomDate(updated) ?: Date()
)