package app.luisramos.ler.ui.feeds

import androidx.test.core.app.launchActivity
import app.luisramos.ler.TestApp
import app.luisramos.ler.data.model.FeedUpdateMode
import app.luisramos.ler.test.DisableAnimationsRule
import app.luisramos.ler.test.robots.backToFeedList
import app.luisramos.ler.test.robots.feedList
import app.luisramos.ler.test.robots.toSideMenu
import app.luisramos.ler.ui.MainActivity
import com.schibsted.spain.barista.interaction.BaristaSleepInteractions
import com.schibsted.spain.barista.interaction.BaristaSleepInteractions.*
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.ClassRule
import org.junit.Test
import java.util.*
import java.util.concurrent.TimeUnit

class FeedListScreenTest {
    companion object {
        @get:ClassRule
        val disableAnimationsRule = DisableAnimationsRule()
    }

    @Test
    fun whenUserChangesFeed_titleShouldUpdate() {
        runBlocking {
            launchActivity<MainActivity>().use {
                insertFakeFeeds()

                feedList {
                    assertTitleIs("All")
                    openSideMenu()
                } toSideMenu {
                    tapSecondFeedInList()
                    sleep(1, TimeUnit.SECONDS)
                } backToFeedList {
                    assertTitleIs("Feed #2")
                }
            }
        }
    }

    private suspend fun insertFakeFeeds() {
        TestApp.testAppContainer.fakeDb.apply {
            insertFeed(
                title = "Feed #1",
                link = "link.com",
                description = "",
                updateLink = "",
                updateMode = FeedUpdateMode.NONE,
                updatedAt = Date(),
                createdAt = Date(),
            )

            insertFeed(
                title = "Feed #2",
                link = "link.com",
                description = "",
                updateLink = "",
                updateMode = FeedUpdateMode.NONE,
                updatedAt = Date(),
                createdAt = Date(),
            )
        }
    }
}