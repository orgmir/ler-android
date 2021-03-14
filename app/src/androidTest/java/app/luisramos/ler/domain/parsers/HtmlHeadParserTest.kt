package app.luisramos.ler.domain.parsers

import org.junit.Assert.assertEquals
import org.junit.Test

class HtmlHeadParserTest {
    private val parser = HtmlHeadParser

    @Test
    fun testParserWorks() {
        val stream = javaClass.classLoader?.getResourceAsStream("orgmir.html")

        val feedLinks = parser.parse(stream!!, "http://orgmir.com")

        assertEquals(2, feedLinks.size)
        val first = feedLinks.first()
        assertEquals(first.title, "The Working Directory")
        assertEquals(first.link, "https://orgmir.com/index.xml")
        val second = feedLinks[1]
        assertEquals(second.title, "The Working Directory #2")
        assertEquals(second.link, "https://orgmir.com/other.xml")
    }
}