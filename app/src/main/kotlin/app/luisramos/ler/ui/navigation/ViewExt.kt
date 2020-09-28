package app.luisramos.ler.ui.navigation

import android.app.Activity
import android.view.Menu
import android.view.View
import app.luisramos.ler.R

val View.activity get() = context as Activity

@Suppress("UNCHECKED_CAST")
fun <T : Activity> View.activity() = context as T

fun View.onCreateOptionsMenu(onCreateOptionsMenu: (Menu) -> Unit) {
    activity<NavigatingActivity>().onCreateOptionsMenu = onCreateOptionsMenu
    activity.invalidateOptionsMenu()
}

fun View.goTo(screen: Screen) {
    activity<NavigatingActivity>().goTo(screen)
}

fun View.goBack() {
    activity<NavigatingActivity>().goBack()
}

fun View.onScreenExiting(block: () -> Unit) {
    @Suppress("UNCHECKED_CAST")
    var callbacks = getTag(R.id.notification_on_screen_exit) as? MutableList<() -> Unit>
    if (callbacks == null) {
        callbacks = mutableListOf()
        setTag(R.id.notification_on_screen_exit, callbacks)
    }
    callbacks.add(block)
}

fun View.notifyScreenExiting() {
    @Suppress("UNCHECKED_CAST")
    val callbacks = getTag(R.id.notification_on_screen_exit) as? MutableList<() -> Unit>
    callbacks?.forEach { it.invoke() }
}