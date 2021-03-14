package app.luisramos.ler.screenshots

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.OngoingStubbing
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import app.luisramos.ler.R
import com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItem
import org.hamcrest.Matcher


fun feedList(func: FeedListRobot.() -> Unit): FeedListRobot {
    return FeedListRobot().apply(func)
}

infix fun FeedListRobot.toSideMenu(func: SideMenuRobot.() -> Unit) = sideMenu(func)

class FeedListRobot {
    fun openSideMenu() {
        onView(withContentDescription(R.string.abc_action_bar_up_description))
            .perform(click())
    }

    fun assertOpensExternalUrl(block: () -> Unit) {
        withIntent(hasAction(Intent.ACTION_VIEW), block)
    }

    fun tapSecondItem() {
        clickListItem(R.id.feedItemsRecyclerView, 1)
    }
}

fun withIntent(matcher: Matcher<Intent>, block: () -> Unit) {
    intending(matcher).ignoreResult()
    block()
}

fun OngoingStubbing.ignoreResult() =
    this.respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))