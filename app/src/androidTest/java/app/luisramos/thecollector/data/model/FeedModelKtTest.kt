package app.luisramos.thecollector.data.model

import app.luisramos.thecollector.parsers.AtomXmlParser
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class FeedModelKtTest {

    @Test
    fun testAtomFeedToFeedModelWorks() {
        val parser = AtomXmlParser()
        val stream = javaClass.classLoader?.getResourceAsStream("feeds.xml")
        val feed = parser.parse(stream!!)

        val feedModel = feed.toFeed()

        val expected = FeedModel(
            title = "Recent Questions - Stack Overflow",
            link = "https://stackoverflow.com/questions",
            description = "most recent 30 from stackoverflow.com",
            updated = parseAtomDateString("2020-02-22T11:20:03Z")!!,
            items = listOf(
                FeedItemModel(
                    id = "https://stackoverflow.com/q/12589792",
                    title = "How to replace substring in mongodb document",
                    description = "I have a lot of mongodb documents in a collection",
                    link = "https://stackoverflow.com/questions/12589792/how-to-replace-substring-in-mongodb-document",
                    published = parseAtomDateString("2012-09-25T19:29:14Z")!!,
                    updated = parseAtomDateString("2020-02-22T11:18:48Z")!!
                ),
                FeedItemModel(
                    id = "https://stackoverflow.com/q/3684463",
                    title = "PHP foreach with Nested Array?",
                    description = "I have a nested array in which I want",
                    link = "https://stackoverflow.com/questions/3684463/php-foreach-with-nested-array",
                    published = parseAtomDateString("2020-02-27T00:00:00-08:00")!!,
                    updated = parseAtomDateString("2020-02-22T11:19:38Z")!!
                )
            )
        )

        assertThat(feedModel, `is`(expected))
    }
}