package app.luisramos.ler.domain

interface SaveNotifyTimePrefUseCase {
    val notifyHourMinute: Pair<Int, Int>
    suspend fun savePref(hour: Int, minute: Int)
}

class DefaultSaveNotifyTimePrefUseCase(
    private val preferences: Preferences
) : SaveNotifyTimePrefUseCase {
    override val notifyHourMinute: Pair<Int, Int>
        get() {
            val time = preferences.newPostNotificationTime
            val hour = time / 100
            val minute = time - hour * 100
            return hour to minute
        }

    override suspend fun savePref(hour: Int, minute: Int) {
        preferences.newPostNotificationTime = hour * 100 + minute
        // TODO reschedule notification work for next time slot
        //  - refresh feeds
        //  - check the notify on feeds if there are new posts
        //  - if we have new posts, post local notif
    }
}