package app.luisramos.thecollector.parsers

import android.util.Xml
import app.luisramos.thecollector.data.model.FeedModel
import app.luisramos.thecollector.data.model.toFeed
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream

class RssAtomCombinedParser {
    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(inputStream: InputStream): FeedModel? {
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