package app.luisramos.ler.domain.parsers

import app.luisramos.ler.domain.parsers.models.Opml
import app.luisramos.ler.domain.parsers.models.Opml.Outline
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class OpmlParserTest {
    private val parser = OpmlParser()

    @Test
    fun testParserWorks() {
        val stream = javaClass.classLoader?.getResourceAsStream("opml.xml")

        val opml = parser.parse(stream!!)

        val expected = Opml(
            title = "Feeds",
            items = listOf(
                Outline(
                    text = "Aggregator News",
                    type = "rss",
                    xmlUrl = "http://tughi.github.io/aggregator-android/news.rss",
                    htmlUrl = "https://tughi.github.io/aggregator-android"
                ),
                Outline(
                    text = "Dan Abramov's Overreacted Blog RSS Feed",
                    type = "rss",
                    xmlUrl = "https://overreacted.io/rss.xml",
                    htmlUrl = "https://overreacted.io"
                ),
                Outline(
                    text = "Go (Golang) Programming Blog - Ardan Labs on",
                    type = "rss",
                    xmlUrl = "https://www.ardanlabs.com/blog/index.xml",
                    htmlUrl = "https://www.ardanlabs.com/blog/"
                ),
                Outline(
                    text = "NSHipster",
                    type = "rss",
                    xmlUrl = "https://nshipster.com/feed.xml",
                    htmlUrl = "https://nshipster.com/"
                ),
                Outline(
                    text = "RWieruch - RSS Feed",
                    type = "rss",
                    xmlUrl = "https://www.robinwieruch.de/index.xml",
                    htmlUrl = "https://www.robinwieruch.de"
                ),
                Outline(
                    text = "Ramblings from Jessie",
                    type = "rss",
                    xmlUrl = "https://blog.jessfraz.com/index.xml",
                    htmlUrl = "https://blog.jessfraz.com/"
                ),
                Outline(
                    text = "Stack Overflow Blog",
                    type = "rss",
                    xmlUrl = "https://stackoverflow.blog/feed/",
                    htmlUrl = "https://stackoverflow.blog"
                ),
                Outline(
                    text = "Tania Rascia",
                    type = "rss",
                    xmlUrl = "https://www.taniarascia.com/rss.xml",
                    htmlUrl = "https://www.taniarascia.com/"
                ),
                Outline(
                    text = "The Beautiful Mess",
                    type = "rss",
                    xmlUrl = "https://cutlefish.substack.com/feed.xml",
                    htmlUrl = "https://cutlefish.substack.com"
                ),
                Outline(
                    text = "The Working Directory",
                    type = "rss",
                    xmlUrl = "https://orgmir.com/index.xml",
                    htmlUrl = "https://orgmir.com/"
                ),
                Outline(
                    text = "qualitytesting.tech » Feed",
                    type = "rss",
                    xmlUrl = "https://qualitytesting.tech/feed/",
                    htmlUrl = "http://qualitytesting.tech/"
                )
            )
        )

        assertThat(opml, `is`(expected))
    }
}