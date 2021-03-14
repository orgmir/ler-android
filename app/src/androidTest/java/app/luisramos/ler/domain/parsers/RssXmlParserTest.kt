package app.luisramos.ler.domain.parsers

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class RssXmlParserTest {

    private val parser = RssXmlParser

    @Test
    fun testParserWorks() {
        val stream = javaClass.classLoader?.getResourceAsStream("rss.xml")

        val channel = parser.parse(stream!!)

        val expected = RssXmlParser.Channel(
            title = "The Working Directory",
            description = "Recent content on The Working Directory",
            link = "https://orgmir.com/",
            lastBuildDate = "Sat, 04 May 2019 08:10:56 +0000",
            items = listOf(
                RssXmlParser.Item(
                    title = "Oh My Zsh prompt theme for Windows Powershell!",
                    link = "https://orgmir.com/posts/windows-powershell-oh-my-zsh/",
                    pubDate = "Sat, 04 May 2019 08:10:56 +0000",
                    guid = "https://orgmir.com/posts/windows-powershell-oh-my-zsh/",
                    description = "I do most of my programming in my mac these days, but once in a while my"
                ),
                RssXmlParser.Item(
                    title = "Auto Layout UIView Extension: A quick way to programatically create layouts",
                    link = "https://orgmir.com/posts/autolayout-uiview-extension/",
                    pubDate = "Tue, 30 Apr 2019 21:54:45 +1000",
                    guid = "https://orgmir.com/posts/autolayout-uiview-extension/",
                    description = "Recently I have taken a renewed interest into this blog. I have revamped"
                ),
                RssXmlParser.Item(
                    title = "Blog revamp: Migrating to Hugo",
                    link = "https://orgmir.com/posts/migration-to-hugo/",
                    pubDate = "Thu, 27 Sep 2018 11:35:31 +1000",
                    guid = "https://orgmir.com/posts/migration-to-hugo/",
                    description = "The blog has a new look! I like to think it looks a bit better now. Of"
                )
            )
        )
        assertThat(channel, `is`(expected))
    }

    @Test
    fun anotherParserTest() {
        val stream = javaClass.classLoader?.getResourceAsStream("luisramosdev.xml")

        val channel = parser.parse(stream!!)

        assertThat(channel, `is`(notNullValue()))
    }
}