package app.luisramos.ler.domain

import android.content.SharedPreferences

interface Preferences {
    var hideReadFeedItems: Boolean
    var isNewPostNotificationEnabled: Boolean
    var newPostNotificationTime: Int
    var isFeedRefreshEnabled: Boolean
}

class DefaultPreferences(preferences: SharedPreferences) : Preferences {
    override var hideReadFeedItems: Boolean by preferences.boolean("showReadFeedItems", false)
    override var isNewPostNotificationEnabled: Boolean
            by preferences.boolean("isNewPostNotificationEnabled", true)
    override var newPostNotificationTime: Int by preferences.int("newPostNotificationTime", 800)
    override var isFeedRefreshEnabled: Boolean by preferences.boolean("isFeedRefreshEnabled", true)
}
