package app.luisramos.ler.test

import app.luisramos.ler.domain.Preferences

class FakePreferences(
    override var hideReadFeedItems: Boolean = false,
    override var newPostNotificationTime: Int = 800
) : Preferences