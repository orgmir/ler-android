package app.luisramos.ler.test

import app.luisramos.ler.domain.Preferences

class FakePreferences(
    override var hideReadFeedItems: Boolean = false,
    override var isNewPostNotificationEnabled: Boolean = true,
    override var newPostNotificationTime: Int = 800,
    override var isFeedRefreshEnabled: Boolean = false
) : Preferences