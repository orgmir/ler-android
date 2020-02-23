package app.luisramos.thecollector.parsers

import org.junit.Assert.assertEquals
import org.junit.Test

class AtomXmlParserTest {

    private val parser = AtomXmlParser()

    @Test
    fun testParserWorks() {
        val stream = javaClass.classLoader?.getResourceAsStream("feeds.xml")

        val feed = parser.parse(stream!!)

        assertEquals(feed.title, "Recent Questions - Stack Overflow")
        assertEquals(feed.link, "https://stackoverflow.com/questions")

        val entry = feed.entries.first()
        assertEquals(entry.title, "How to replace substring in mongodb document")
        assertEquals(
            entry.link,
            "https://stackoverflow.com/questions/12589792/how-to-replace-substring-in-mongodb-document"
        )
    }
}
