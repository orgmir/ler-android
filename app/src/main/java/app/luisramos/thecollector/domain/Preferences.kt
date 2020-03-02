package app.luisramos.thecollector.domain

import android.content.SharedPreferences
import androidx.core.content.edit

interface Preferences {
    var showReadFeedItems: Boolean
}

class DefaultPreferences(val preferences: SharedPreferences) : Preferences {
    override var showReadFeedItems: Boolean
        get() = preferences.getBoolean("showReadFeedItems", true)
        set(value) {
            preferences.edit(commit = true) { putBoolean("showReadFeedItems", value) }
        }
}