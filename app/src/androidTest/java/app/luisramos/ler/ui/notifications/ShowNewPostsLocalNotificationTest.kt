package app.luisramos.ler.ui.notifications

import androidx.test.core.app.launchActivity
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import app.luisramos.ler.App
import app.luisramos.ler.data.model.FeedUpdateMode
import app.luisramos.ler.domain.Db
import app.luisramos.ler.test.DisableAnimationsRule
import app.luisramos.ler.test.robots.feedList
import app.luisramos.ler.ui.MainActivity
import com.google.common.truth.Truth.assertThat
import com.schibsted.spain.barista.rule.cleardata.ClearDatabaseRule
import kotlinx.coroutines.runBlocking
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import java.util.*

class ShowNewPostsLocalNotificationTest {

    companion object {
        @get:ClassRule
        val disableAnimationsRule = DisableAnimationsRule()
    }

    @get:Rule
    val clearDbRule = ClearDatabaseRule()

    private val targetContext by lazy { InstrumentationRegistry.getInstrumentation().targetContext }
    private val appContainer by lazy {
        val app = targetContext.applicationContext as App
        app.appContainer
    }
    private val uiDevice by lazy {
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    }

    @Test
    fun whenThereAreNewPosts_shouldShowNotification() {
        launchActivity<MainActivity>().use {
            runBlocking {
                setupFeedsInDb()
                triggerNotification()
            }
        }

        uiDevice.openNotification()

        uiDevice.tapNewPostsNotification()

        feedList {
            assertTitleIs("All")
        }
    }

    private suspend fun triggerNotification() {
        appContainer.showNewPostsLocalNotifUseCase.showLocalNotif()
    }

    private suspend fun setupFeedsInDb() {
        val db = appContainer.db
        val feedId = db.insertFeed(
            title = "Test Feed",
            link = "http://test.feed",
            description = "test test",
            updateLink = "http://test.feed",
            updateMode = FeedUpdateMode.NONE,
            updatedAt = Date()
        )
        db.toggleFeedNotify(feedId)
        insertFeedItem(db, feedId, 1, Date())
        insertFeedItem(db, feedId, 2, halfDayAgoDate())
    }

    private suspend fun insertFeedItem(db: Db, feedId: Long, number: Int, createdAt: Date) {
        db.insertFeedItem(
            title = "Test Post #$number",
            description = "test test",
            link = "http://test.feed",
            publishedAt = Date(),
            updatedAt = Date(),
            createdAt = createdAt,
            feedId = feedId
        )
    }

    private fun halfDayAgoDate(): Date = Calendar.getInstance().run {
        add(Calendar.HOUR_OF_DAY, -12)
        time
    }

    private fun UiDevice.tapNewPostsNotification() {
        val notifTitle = By.text("Ler")
        wait(Until.hasObject(notifTitle), 5000)

        val titleObject = findObject(notifTitle)
        assertThat(titleObject).isNotNull()
        val descriptionObject = findObject(By.text("New post from Test Feed."))
        assertThat(descriptionObject).isNotNull()
        descriptionObject.click()

        // wait for app to open
        wait(Until.hasObject(By.textStartsWith("All")), 5000)
    }
}