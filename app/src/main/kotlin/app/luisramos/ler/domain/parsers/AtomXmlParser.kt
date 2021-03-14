package app.luisramos.ler.domain.parsers

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream

private val ns: String? = null

object AtomXmlParser {
    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(inputStream: InputStream): Feed {
        inputStream.use {
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return readFeed(parser)
        }
    }


    @Throws(XmlPullParserException::class, IOException::class)
    fun readFeed(parser: XmlPullParser): Feed {
        var title: String? = null
        var link: String? = null
        var subtitle: String? = null
        var updated: String? = null
        val entries = mutableListOf<Entry>()

        parser.require(XmlPullParser.START_TAG, ns, "feed")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            when (parser.name) {
                "title" -> title = readTag("title", parser)
                "link" -> link = readLink(parser)
                "subtitle" -> subtitle = readTag("subtitle", parser)
                "updated" -> updated = readTag("updated", parser)
                "entry" -> entries.add(readEntry(parser))
                else -> skip(parser)
            }
        }

        return Feed(
            title = title,
            link = link,
            subtitle = subtitle,
            updated = updated,
            entries = entries
        )
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readEntry(parser: XmlPullParser): Entry {
        parser.require(XmlPullParser.START_TAG, ns, "entry")
        var title: String? = null
        var summary: String? = null
        var link: String? = null
        var id: String? = null
        var published: String? = null
        var updated: String? = null
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                "title" -> title = readTag("title", parser)
                "summary" -> summary = readTag("summary", parser)
                "link" -> link = readLink(parser)
                "id" -> id = readTag("id", parser)
                "published" -> published = readTag("published", parser)
                "updated" -> updated = readTag("updated", parser)
                else -> skip(parser)
            }
        }
        return Entry(
            title = title,
            summary = summary,
            link = link,
            id = id,
            published = published,
            updated = updated
        )
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readTag(tag: String, parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, tag)
        val text = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, tag)
        return text
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readLink(parser: XmlPullParser): String {
        var link = ""
        parser.require(XmlPullParser.START_TAG, ns, "link")
        val tag = parser.name
        val relType = parser.getAttributeValue(null, "rel")
        if (tag == "link" && (relType == "alternate" || relType != "self")) {
            link = parser.getAttributeValue(null, "href")
            parser.nextTag()
        } else if (parser.eventType != XmlPullParser.END_TAG) {
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, ns, "link")
        return link
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
}

data class Feed(
    val title: String? = null,
    val link: String? = null,
    val subtitle: String? = null,
    val updated: String? = null,
    val entries: List<Entry> = listOf()
)

data class Entry(
    val id: String?,
    val title: String?,
    val summary: String?,
    val link: String?,
    val published: String?,
    val updated: String?
)
