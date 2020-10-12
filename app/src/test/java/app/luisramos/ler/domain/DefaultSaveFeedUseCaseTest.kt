package app.luisramos.ler.domain

import app.luisramos.ler.data.model.FeedItemModel
import app.luisramos.ler.data.model.FeedModel
import app.luisramos.ler.data.model.parseAtomDateString
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import java.io.IOException
import java.util.*

class DefaultSaveFeedUseCaseTest {

    private val db = FakeDb()
    private val useCase = DefaultSaveFeedUseCase(db)

    @Test
    fun `given feed with empty title or link, should return error`() = runBlockingTest {
        val model = FeedModel(
            title = "",
            link = "",
            updated = Date()
        )
        val result = useCase.saveFeed(model)
        assertThat(result.exceptionOrNull()).isInstanceOf(IOException::class.java)
    }

    @Test
    fun `feeds should be saved correctly`() = runBlockingTest {
        val model = FeedModel(
            title = "Recent Questions - Stack Overflow",
            link = "https://stackoverflow.com/questions",
            description = "most recent 30 from stackoverflow.com",
            feedLink = "https://stackoverflow.com/questions",
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
                    published = parseAtomDateString("2020-02-27T00:00:00Z")!!,
                    updated = parseAtomDateString("2020-02-22T11:19:38Z")!!
                )
            )
        )

        useCase.saveFeed(model)

        val feed = db.findFeedByUpdateLink(model.link)
        assertThat(feed?.title).isEqualTo(model.title)

        val feedItems = db.findFeedItemsIdsByFeedId(feed?.id ?: -1L)
        assertThat(feedItems.size).isEqualTo(2)
    }

    @Test
    fun `updating a feed should update items`() = runBlockingTest {
        val initialModel = FeedModel(
            title = "Recent Questions - Stack Overflow",
            link = "https://stackoverflow.com/questions",
            description = "most recent 30 from stackoverflow.com",
            feedLink = "https://stackoverflow.com/questions",
            updated = parseAtomDateString("2020-02-22T11:20:03Z")!!,
            items = listOf(
                FeedItemModel(
                    id = "https://stackoverflow.com/q/3684463",
                    title = "PHP foreach with Nested Array?",
                    description = "I have a nested array in which I want",
                    link = "https://stackoverflow.com/questions/3684463/php-foreach-with-nested-array",
                    published = parseAtomDateString("2020-02-27T00:00:00Z")!!,
                    updated = parseAtomDateString("2020-02-22T11:19:38Z")!!
                )
            )
        )
        val updatedModel = FeedModel(
            title = "Recent Questions - Stack Overflow",
            link = "https://stackoverflow.com/questions",
            description = "most recent 30 from stackoverflow.com",
            feedLink = "https://stackoverflow.com/questions",
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
                    published = parseAtomDateString("2020-02-27T00:00:00Z")!!,
                    updated = parseAtomDateString("2020-02-22T11:19:38Z")!!
                )
            )
        )

        useCase.saveFeed(initialModel)
        useCase.saveFeed(updatedModel)

        val feed = db.findFeedByUpdateLink(updatedModel.link)
        assertThat(feed).isNotNull()
        val feedItems = db.findFeedItemsIdsByFeedId(feed?.id ?: -1L)
        assertThat(feedItems.size).isEqualTo(2)
    }
}

