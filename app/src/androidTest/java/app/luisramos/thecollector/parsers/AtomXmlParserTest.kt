package app.luisramos.thecollector.parsers

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test

class AtomXmlParserTest {

    private val parser = AtomXmlParser()

    @Test
    fun testParserWorks() {
        val stream = javaClass.classLoader?.getResourceAsStream("feeds.xml")

        val feed = parser.parse(stream!!)

        val expected = AtomXmlParser.Feed(
            title = "Recent Questions - Stack Overflow",
            link = "https://stackoverflow.com/questions",
            subtitle = "most recent 30 from stackoverflow.com",
            updated = "2020-02-22T11:20:03Z",
            entries = listOf(
                AtomXmlParser.Entry(
                    id = "https://stackoverflow.com/q/12589792",
                    title = "How to replace substring in mongodb document",
                    summary = "I have a lot of mongodb documents in a collection",
                    link = "https://stackoverflow.com/questions/12589792/how-to-replace-substring-in-mongodb-document",
                    published = "2012-09-25T19:29:14Z",
                    updated = "2020-02-22T11:18:48Z"
                ),
                AtomXmlParser.Entry(
                    id = "https://stackoverflow.com/q/3684463",
                    title = "PHP foreach with Nested Array?",
                    summary = "I have a nested array in which I want",
                    link = "https://stackoverflow.com/questions/3684463/php-foreach-with-nested-array",
                    published = "2010-09-10T12:08:59Z",
                    updated = "2020-02-22T11:19:38Z"
                )
            )
        )
        assertThat(feed, `is`(expected))
    }
}
