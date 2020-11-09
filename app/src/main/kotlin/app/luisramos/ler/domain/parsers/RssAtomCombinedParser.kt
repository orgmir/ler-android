package app.luisramos.ler.domain.parsers

import android.util.Xml
import app.luisramos.ler.data.model.FeedModel
import app.luisramos.ler.data.model.toFeedModel
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream

class RssAtomCombinedParser : FeedParser {

    private val nameParser = Xml.newPullParser()
    private val rssParser = RssXmlParser()
    private val atomParser = AtomXmlParser()

    @Throws(XmlPullParserException::class, IOException::class)
    override fun parse(inputStream: InputStream): FeedModel? {
        nameParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        nameParser.setInput(inputStream, null)
        nameParser.nextTag()
        return when (nameParser.name) {
            "rss" -> rssParser.parse(inputStream).toFeedModel()
            "feed" -> atomParser.parse(inputStream).toFeedModel()
            else -> null
        }

    }
}