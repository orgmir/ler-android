package app.luisramos.ler.domain.parsers

import android.util.Xml
import app.luisramos.ler.domain.parsers.models.Opml
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream

class OpmlParser {

    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(inputStream: InputStream): Opml {
        inputStream.use {
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag() // opml
            return readOpml(parser)
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readOpml(parser: XmlPullParser): Opml {
        var title = ""
        var items = emptyList<Opml.Outline>()

        parser.require(XmlPullParser.START_TAG, null, "opml")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            when (parser.name) {
                "head" -> title = readTitle(parser)
                "body" -> items = readItems(parser)
                else -> skip(parser)
            }
        }

        return Opml(title, items)
    }

    private fun readTitle(parser: XmlPullParser): String {
        var title = ""

        parser.require(XmlPullParser.START_TAG, null, "head")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            when (parser.name) {
                "title" -> title = readTag("title", parser)
                else -> skip(parser)
            }
        }

        return title
    }

    private fun readItems(parser: XmlPullParser): List<Opml.Outline> {
        val items = mutableListOf<Opml.Outline>()

        parser.require(XmlPullParser.START_TAG, null, "body")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                "outline" -> items.add(readOutline(parser))
                else -> skip(parser)
            }
        }

        return items
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readOutline(parser: XmlPullParser): Opml.Outline {
        parser.require(XmlPullParser.START_TAG, null, "outline")
        val text = parser.getAttributeValue(null, "text") ?: ""
        val type = parser.getAttributeValue(null, "type") ?: ""
        val xmlUrl = parser.getAttributeValue(null, "xmlUrl") ?: ""
        val htmlUrl = parser.getAttributeValue(null, "htmlUrl") ?: ""

        parser.nextTag()
        parser.require(XmlPullParser.END_TAG, null, "outline")

        return Opml.Outline(text, type, xmlUrl, htmlUrl)
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
}