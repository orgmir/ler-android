package app.luisramos.ler.domain.parsers

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream

object RssXmlParser {
    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(inputStream: InputStream): Channel {
        inputStream.use {
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag() //skip rss
            parser.nextTag()
            return readChannel(parser)
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    fun readChannel(parser: XmlPullParser): Channel {
        var title: String? = null
        var link: String? = null
        var description: String? = null
        var lastBuildDate: String? = null
        val items = mutableListOf<Item>()

        parser.require(XmlPullParser.START_TAG, null, "channel")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            when (parser.name) {
                "title" -> title = readTag("title", parser)
                "link" -> link = readTag("link", parser)
                "lastBuildDate" -> lastBuildDate = readTag("lastBuildDate", parser)
                "description" -> description = readTag("description", parser)
                "item" -> items.add(readItem(parser))
                else -> skip(parser)
            }
        }

        return Channel(
            title = title,
            description = description,
            link = link,
            lastBuildDate = lastBuildDate,
            items = items
        )
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readItem(parser: XmlPullParser): Item {
        var title: String? = null
        var link: String? = null
        var description: String? = null
        var pubDate: String? = null
        var guid: String? = null

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            when (parser.name) {
                "title" -> title = readTag("title", parser)
                "link" -> link = readTag("link", parser)
                "description" -> description = readTag("description", parser)
                "pubDate" -> pubDate = readTag("pubDate", parser)
                "guid" -> guid = readTag("guid", parser)
                else -> skip(parser)
            }
        }

        return Item(
            title = title,
            link = link,
            guid = guid,
            description = description,
            pubDate = pubDate
        )
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readTag(tag: String, parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, null, tag)
        val text = readText(parser)
        parser.require(XmlPullParser.END_TAG, null, tag)
        return text
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }

    data class Channel(
        val title: String?,
        val description: String?,
        val link: String?,
        val lastBuildDate: String?,
        val items: List<Item>
    )

    data class Item(
        val title: String?,
        val link: String?,
        val guid: String?,
        val description: String?,
        val pubDate: String?
    )
}