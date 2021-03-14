package app.luisramos.ler.screenshots

import app.luisramos.ler.R
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn

fun sideMenu(func: SideMenuRobot.() -> Unit): SideMenuRobot {
    return SideMenuRobot().apply(func)
}

infix fun SideMenuRobot.toAddSubscription(func: AddSubscriptionRobot.() -> Unit) =
    addSubscription(func)

class SideMenuRobot {
    fun tapAddSubscription() {
        clickOn(R.string.add_subscription)
    }
}