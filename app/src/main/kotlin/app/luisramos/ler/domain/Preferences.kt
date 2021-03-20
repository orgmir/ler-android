package app.luisramos.ler.domain

import android.content.SharedPreferences

interface Preferences {
    var showReadFeedItems: Boolean
    var newPostNotificationTime: Int
}

class DefaultPreferences(preferences: SharedPreferences) : Preferences {
    override var showReadFeedItems: Boolean by preferences.boolean("showReadFeedItems", false)
    override var newPostNotificationTime: Int by preferences.int("newPostNotificationTime", 800)
}
