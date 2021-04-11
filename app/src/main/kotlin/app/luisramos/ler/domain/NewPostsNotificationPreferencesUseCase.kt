package app.luisramos.ler.domain

interface NewPostsNotificationPreferencesUseCase {
    var isNotificationEnabled: Boolean
    val notifyHourMinute: Pair<Int, Int>
    suspend fun savePref(hour: Int, minute: Int)
}

class DefaultNewPostsNotificationPreferencesUseCase(
    private val useCase: ScheduleNewPostsNotifUseCase,
    private val cancelUseCase: CancelNewPostsNotificationUseCase,
    private val preferences: Preferences
) : NewPostsNotificationPreferencesUseCase {
    override var isNotificationEnabled: Boolean
        get() = preferences.isNewPostNotificationEnabled
        set(value) {
            preferences.isNewPostNotificationEnabled = value
            if (value) {
                val (hour, minute) = notifyHourMinute
                useCase.schedule(hour, minute)
            } else {
                cancelUseCase.cancelScheduledNotification()
            }
        }

    override val notifyHourMinute: Pair<Int, Int>
        get() {
            val time = preferences.newPostNotificationTime
            val hour = time / 100
            val minute = time - hour * 100
            return hour to minute
        }

    override suspend fun savePref(hour: Int, minute: Int) {
        preferences.newPostNotificationTime = hour * 100 + minute
        useCase.schedule(hour, minute)
    }
}