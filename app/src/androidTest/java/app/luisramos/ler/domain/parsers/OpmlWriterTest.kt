package app.luisramos.ler.domain.parsers

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.io.ByteArrayOutputStream

@ExperimentalStdlibApi
class OpmlWriterTest {

    @Test
    fun writerShouldWriteToFileProperly() {
        val inputStream = javaClass.classLoader?.getResourceAsStream("opml.xml")
        val opml = OpmlParser.parse(inputStream!!)

        val outputStream = ByteArrayOutputStream()
        OpmlWriter().write(outputStream, opml)

        val actual = outputStream.toByteArray().decodeToString()
        val expected =
            "<opml version=\"2.0\"><head><title>Feeds</title></head><body><outline " +
                    "text=\"Aggregator News\" type=\"rss\" htmlUrl=\"https://tughi." +
                    "github.io/aggregator-android\" xmlUrl=\"http://tughi.github.io" +
                    "/aggregator-android/news.rss\"/><outline text=\"Dan Abramov's " +
                    "Overreacted Blog RSS Feed\" type=\"rss\" htmlUrl=\"https://ove" +
                    "rreacted.io\" xmlUrl=\"https://overreacted.io/rss.xml\"/><outl" +
                    "ine text=\"Go (Golang) Programming Blog - Ardan Labs on\" type" +
                    "=\"rss\" htmlUrl=\"https://www.ardanlabs.com/blog/\" xmlUrl=\"" +
                    "https://www.ardanlabs.com/blog/index.xml\"/><outline text=\"NS" +
                    "Hipster\" type=\"rss\" htmlUrl=\"https://nshipster.com/\" xmlU" +
                    "rl=\"https://nshipster.com/feed.xml\"/><outline text=\"RWieruc" +
                    "h - RSS Feed\" type=\"rss\" htmlUrl=\"https://www.robinwieruch" +
                    ".de\" xmlUrl=\"https://www.robinwieruch.de/index.xml\"/><outli" +
                    "ne text=\"Ramblings from Jessie\" type=\"rss\" htmlUrl=\"https" +
                    "://blog.jessfraz.com/\" xmlUrl=\"https://blog.jessfraz.com/ind" +
                    "ex.xml\"/><outline text=\"Stack Overflow Blog\" type=\"rss\" h" +
                    "tmlUrl=\"https://stackoverflow.blog\" xmlUrl=\"https://stackov" +
                    "erflow.blog/feed/\"/><outline text=\"Tania Rascia\" type=\"rss" +
                    "\" htmlUrl=\"https://www.taniarascia.com/\" xmlUrl=\"https://w" +
                    "ww.taniarascia.com/rss.xml\"/><outline text=\"The Beautiful Me" +
                    "ss\" type=\"rss\" htmlUrl=\"https://cutlefish.substack.com\" x" +
                    "mlUrl=\"https://cutlefish.substack.com/feed.xml\"/><outline te" +
                    "xt=\"The Working Directory\" type=\"rss\" htmlUrl=\"https://or" +
                    "gmir.com/\" xmlUrl=\"https://orgmir.com/index.xml\"/><outline " +
                    "text=\"qualitytesting.tech » Feed\" type=\"rss\" htmlUrl=\"htt" +
                    "p://qualitytesting.tech/\" xmlUrl=\"https://qualitytesting.tec" +
                    "h/feed/\"/></body></opml>"
        assertThat(actual, `is`(expected))
    }
}