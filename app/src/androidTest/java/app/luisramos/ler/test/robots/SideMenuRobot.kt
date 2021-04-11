package app.luisramos.ler.test.robots

import app.luisramos.ler.R
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItem

fun sideMenu(func: SideMenuRobot.() -> Unit): SideMenuRobot {
    return SideMenuRobot().apply(func)
}

infix fun SideMenuRobot.toAddSubscription(func: AddSubscriptionRobot.() -> Unit) =
    addSubscription(func)

infix fun SideMenuRobot.backToFeedList(func: FeedListRobot.() -> Unit) = feedList(func)

class SideMenuRobot {
    fun tapAddSubscription() {
        clickOn(R.string.add_subscription)
    }

    fun tapSecondFeedInList() {
        clickListItem(R.id.sideMenuRecyclerView, 1)
    }
}