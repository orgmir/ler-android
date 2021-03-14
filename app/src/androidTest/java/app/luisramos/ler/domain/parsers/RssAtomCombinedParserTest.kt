package app.luisramos.ler.domain.parsers

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class RssAtomCombinedParserTest {
    @Test
    fun testCombinedParsers() {
        val parser = RssAtomCombinedParser

        val stream = javaClass.classLoader?.getResourceAsStream("luisramosdev.xml")
        val feed = parser.parse(stream!!)
        assertThat(feed, `is`(notNullValue()))

        val feedsStream = javaClass.classLoader?.getResourceAsStream("feeds.xml")
        val atomFeed = parser.parse(feedsStream!!)
        assertThat(atomFeed, `is`(notNullValue()))
    }
}