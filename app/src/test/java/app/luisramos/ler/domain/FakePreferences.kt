package app.luisramos.ler.domain

class FakePreferences(
    override var hideReadFeedItems: Boolean = false,
    override var newPostNotificationTime: Int = 800
) : Preferences