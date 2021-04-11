package app.luisramos.ler.domain

import app.luisramos.ler.data.model.FeedUpdateMode
import app.luisramos.ler.test.FakeDb
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import java.util.*

class DefaultRefreshFeedsUseCaseTest {

    private val db = FakeDb()
    private val fetchUseCase = FakeFetchAndSaveChannelUseCase()
    private val useCase = DefaultRefreshFeedsUseCase(db, fetchUseCase)

    @Test
    fun `refreshing feeds should fetch and save them correctly`() = runBlockingTest {
        db.insertFeed(
            title = "Recent Questions - Stack Overflow",
            link = "https://stackoverflow.com/questions",
            description = "most recent 30 from stackoverflow.com",
            updateLink = "https://stackoverflow.com/questions",
            updateMode = FeedUpdateMode.NONE,
            updatedAt = Date()
        )
        db.insertFeed(
            title = "Luis Ramos Blog",
            link = "https://luisramos.dev/feed.xml",
            description = "Luis Ramos Blog",
            updateLink = "https://luisramos.dev/feed.xml",
            updateMode = FeedUpdateMode.NONE,
            updatedAt = Date()
        )

        var updates = arrayOf<Float>()
        useCase.refreshFeedsUseCase {
            updates += it
        }

        assertThat(fetchUseCase.didCallFetchAndSave).isEqualTo(
            arrayOf(
                "https://stackoverflow.com/questions",
                "https://luisramos.dev/feed.xml"
            )
        )
        assertThat(updates).isEqualTo(arrayOf(0f, 0.5f, 1f))
    }
}