package app.luisramos.ler.domain.parsers

import android.util.Xml
import app.luisramos.ler.data.model.FeedModel
import app.luisramos.ler.data.model.toFeed
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream

interface FeedParser {
    fun parse(inputStream: InputStream): FeedModel?
}

class RssAtomCombinedParser : FeedParser {
    @Throws(XmlPullParserException::class, IOException::class)
    override fun parse(inputStream: InputStream): FeedModel? {
        inputStream.use {
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return when (parser.name) {
                "rss" -> {
                    parser.nextTag()
                    RssXmlParser().readChannel(parser).toFeed()
                }
                "feed" -> AtomXmlParser().readFeed(parser).toFeed()
                else -> null
            }
        }
    }
}