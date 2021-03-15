package app.luisramos.ler.data.model

import app.luisramos.ler.domain.parsers.Entry
import app.luisramos.ler.domain.parsers.Feed
import app.luisramos.ler.domain.parsers.RssXmlParser
import timber.log.Timber
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

data class FeedModel(
    val title: String,
    val link: String,
    val feedLink: String = "",
    val description: String? = null,
    val updated: Date,
    val items: List<FeedItemModel> = listOf()
)

fun parseRssDate(dateString: String?): Date? {
    dateString ?: return null
    return try {
        val localDateTime = LocalDateTime.parse(dateString, DateTimeFormatter.RFC_1123_DATE_TIME)
        Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
    } catch (e: DateTimeParseException) {
        Timber.e(e)
        null
    }
}

fun parseAtomDate(dateString: String?): Date? {
    dateString ?: return null
    return try {
        val localDateTime = OffsetDateTime.parse(dateString).toLocalDateTime()
        Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
    } catch (e: DateTimeParseException) {
        Timber.e(e)
        null
    }
}

fun RssXmlParser.Channel.toFeedModel() = FeedModel(
    title = title ?: "",
    link = link ?: "",
    description = description,
    updated = lastBuildDate?.let { parseRssDate(it) } ?: Date(),
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
    published = pubDate?.let { parseRssDate(it) } ?: Date(),
    updated = null
)

fun Entry.toFeedItem(): FeedItemModel {
    // If there is no published date, we want the published date
    // to be the updated date
    val updated = parseAtomDate(updated) ?: Date()
    val published = parseAtomDate(published) ?: updated
    return FeedItemModel(
        id = id ?: "",
        title = title ?: "",
        description = summary,
        link = link ?: "",
        published = published,
        updated = updated
    )
}