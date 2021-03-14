package app.luisramos.ler.screenshots

import app.luisramos.ler.R
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaEditTextInteractions.writeTo
import com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItem

fun addSubscription(func: AddSubscriptionRobot.() -> Unit): AddSubscriptionRobot {
    return AddSubscriptionRobot().apply(func)
}

infix fun AddSubscriptionRobot.backToFeedList(func: FeedListRobot.() -> Unit) = feedList(func)

class AddSubscriptionRobot {
    fun typeUrlInSearchBar(feedUrl: String) {
        writeTo(R.id.editText, feedUrl)
    }

    fun submitSearch() {
        clickOn(R.id.searchButton)
    }

    fun selectFirstResult() {
        clickListItem(R.id.subscriptionRecyclerView, 0)
    }
}