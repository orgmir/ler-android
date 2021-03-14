package app.luisramos.ler.data.model

import app.luisramos.ler.domain.parsers.AtomXmlParser
import app.luisramos.ler.domain.parsers.Entry
import app.luisramos.ler.domain.parsers.Feed
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class FeedModelTest {

    @Test
    fun testAtomFeedToFeedModelWorks() {
        val parser = AtomXmlParser
        val stream = javaClass.classLoader?.getResourceAsStream("feeds.xml")
        val feedModel = parser.parse(stream!!)

        val expected = Feed(
            title = "Recent Questions - Stack Overflow",
            link = "https://stackoverflow.com/questions",
            subtitle = "most recent 30 from stackoverflow.com",
            updated = "2020-02-22T11:20:03Z",
            entries = listOf(
                Entry(
                    id = "https://stackoverflow.com/q/12589792",
                    title = "How to replace substring in mongodb document",
                    summary = "I have a lot of mongodb documents in a collection",
                    link = "https://stackoverflow.com/questions/12589792/how-to-replace-substring-in-mongodb-document",
                    published = "2012-09-25T19:29:14Z",
                    updated = "2020-02-22T11:18:48Z"
                ),
                Entry(
                    id = "https://stackoverflow.com/q/3684463",
                    title = "PHP foreach with Nested Array?",
                    summary = "I have a nested array in which I want",
                    link = "https://stackoverflow.com/questions/3684463/php-foreach-with-nested-array",
                    published = "2020-02-27T00:00:00-08:00",
                    updated = "2020-02-22T11:19:38Z"
                )
            )
        )

        assertThat(feedModel, `is`(expected))
    }

    @Test
    fun testRssFeedDateFormatter() {
        assertThat(
            rssFeedDateFormatter.parse("Thu, 17 Sep 2020 00:51:19 GMT"),
            `is`(notNullValue())
        )
        assertThat(
            rssFeedDateFormatter.parse("Sat, 04 May 2019 08:10:56 +0000"),
            `is`(notNullValue())
        )
    }
}