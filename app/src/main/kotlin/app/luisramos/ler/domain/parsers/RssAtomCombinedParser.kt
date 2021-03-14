package app.luisramos.ler.domain.parsers

import android.util.Xml
import app.luisramos.ler.data.model.FeedModel
import app.luisramos.ler.data.model.toFeedModel
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream

object RssAtomCombinedParser : FeedParser {

    @Throws(XmlPullParserException::class, IOException::class)
    override fun parse(inputStream: InputStream): FeedModel? {
        val parser = Xml.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.setInput(inputStream, null)
        parser.nextTag()
        return when (parser.name) {
            "rss" -> {
                parser.nextTag() // skip rss tag
                RssXmlParser.readChannel(parser).toFeedModel()
            }
            "feed" -> AtomXmlParser.readFeed(parser).toFeedModel()
            else -> null
        }
    }
}