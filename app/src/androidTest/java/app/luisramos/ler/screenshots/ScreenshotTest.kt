package app.luisramos.ler.screenshots

import androidx.test.core.app.launchActivity
import androidx.test.filters.LargeTest
import app.luisramos.ler.test.CleanStatusBarRule
import app.luisramos.ler.test.DisableAnimationsRule
import app.luisramos.ler.test.IntentsTestRule
import app.luisramos.ler.test.robots.backToFeedList
import app.luisramos.ler.test.robots.feedList
import app.luisramos.ler.test.robots.toAddSubscription
import app.luisramos.ler.test.robots.toSideMenu
import app.luisramos.ler.ui.MainActivity
import com.schibsted.spain.barista.interaction.BaristaSleepInteractions.sleep
import com.schibsted.spain.barista.rule.cleardata.ClearDatabaseRule
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import tools.fastlane.screengrab.Screengrab
import java.util.concurrent.TimeUnit.SECONDS

@LargeTest
class ScreenshotTest {

    companion object {
        @get:ClassRule
        val cleanStatusBarRule = CleanStatusBarRule()

        @get:ClassRule
        val disableAnimationsRule = DisableAnimationsRule()
    }

    @get:Rule
    val clearDbRule = ClearDatabaseRule()

    @get:Rule
    val intentsTestRule = IntentsTestRule()

    @Test
    fun takeScreenshots() {
        launchActivity<MainActivity>().use {
            feedList {
                openSideMenu()
            } toSideMenu {
                tapAddSubscription()
            } toAddSubscription {
                typeUrlInSearchBar("https://luisramos.dev")
                submitSearch()
                sleep(1, SECONDS)
                Screengrab.screenshot("2_add_subscription")
                selectFirstResult()
            } backToFeedList {
                sleep(4, SECONDS)
                Screengrab.screenshot("1_feed_list")
                openSideMenu()
            } toSideMenu {
                sleep(1, SECONDS)
                Screengrab.screenshot("3_side_menu")
            }
        }
    }
}
