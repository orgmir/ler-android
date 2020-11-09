package app.luisramos.ler.domain

import app.luisramos.ler.data.model.FeedModel
import app.luisramos.ler.domain.parsers.FeedParser
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import java.io.InputStream
import java.util.*

class DefaultFetchChannelUseCaseTest {

    private val parser = StubFeedParser()

    @Test
    fun `given a successful parsing, when fetching channel, should return feed model`() =
        runBlockingTest {
            val useCase = DefaultFetchChannelUseCase(parser, coroutineContext)
            val result = useCase.fetchChannel("http://example.com")
            assertThat(result.value).isEqualTo(parser.mockFeedModel.copy(feedLink = "http://example.com"))
        }

    class StubFeedParser : FeedParser {
        var mockFeedModel = FeedModel("title", "http://example.com", updated = Date())
        override fun parse(inputStream: InputStream): FeedModel? = mockFeedModel
    }
}