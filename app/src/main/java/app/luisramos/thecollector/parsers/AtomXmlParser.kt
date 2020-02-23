package app.luisramos.thecollector.parsers

import android.util.Xml
import com.prof.rssparser.Image
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream

private val ns: String? = null

class AtomXmlParser {
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
    private fun readFeed(parser: XmlPullParser): Feed {
        var title: String? = null
        var link: String? = null
        val entries = mutableListOf<Entry>()

        parser.require(XmlPullParser.START_TAG, ns, "feed")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            when (parser.name) {
                "title" -> title = readTitle(parser)
                "link" -> link = readLink(parser)
                "entry" -> entries.add(readEntry(parser))
                else -> skip(parser)
            }
        }

        return Feed(title, link, null, null, entries)
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readEntry(parser: XmlPullParser): Entry {
        parser.require(XmlPullParser.START_TAG, ns, "entry")
        var title: String? = null
        var summary: String? = null
        var link: String? = null
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                "title" -> title = readTitle(parser)
                "summary" -> summary = readSummary(parser)
                "link" -> link = readLink(parser)
                else -> skip(parser)
            }
        }
        return Entry(
            title,
            summary,
            link
        )
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readTitle(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "title")
        val title = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "title")
        return title
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readLink(parser: XmlPullParser): String {
        var link = ""
        parser.require(XmlPullParser.START_TAG, ns, "link")
        val tag = parser.name
        val relType = parser.getAttributeValue(null, "rel")
        if (tag == "link" && relType == "alternate") {
            link = parser.getAttributeValue(null, "href")
            parser.nextTag()
        } else if (parser.getEventType() != XmlPullParser.END_TAG) {
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, ns, "link")
        return link
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readSummary(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "summary")
        val summary = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "summary")
        return summary
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

    data class Feed(
        val title: String? = null,
        val link: String? = null,
        val description: String? = null,
        val image: Image? = null,
        val entries: List<Entry> = listOf()
    )

    data class Entry(
        val title: String?,
        val summary: String?,
        val link: String?
    )
}