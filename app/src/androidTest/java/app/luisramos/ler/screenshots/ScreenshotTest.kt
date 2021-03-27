package app.luisramos.ler.screenshots

import android.app.Activity
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import app.luisramos.ler.test.CleanStatusBarRule
import app.luisramos.ler.test.DisableAnimationsRule
import app.luisramos.ler.test.IntentsTestRule
import app.luisramos.ler.ui.MainActivity
import com.schibsted.spain.barista.interaction.BaristaSleepInteractions.sleep
import com.schibsted.spain.barista.rule.cleardata.ClearDatabaseRule
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
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
                selectFirstResult()
            } backToFeedList {
                sleep(1, SECONDS)
                assertOpensExternalUrl {
                    tapSecondItem()
                }
                Screengrab.screenshot("feed_list")
                sleep(1, SECONDS)
            }
        }
    }
}

val <A : Activity> ActivityScenario<A>.activity: A
    get() {
        var temp: A? = null
        this.onActivity { temp = it }
        return temp!!
    }

