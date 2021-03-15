package app.luisramos.ler.data.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class FeedModelKtTest {

    @Test
    fun testRssFeedDateFormatter() {
        assertThat(parseRssDate("Thu, 17 Sep 2020 00:51:19 GMT")).isNotNull()
        assertThat(parseRssDate("Sat, 04 May 2019 08:10:56 +0000")).isNotNull()
    }

    @Test
    fun `should create Date objects correctly`() {
        assertThat(parseAtomDate("2021-01-21T00:00:00+00:00")).isNotNull()
    }
}