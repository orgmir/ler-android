package app.luisramos.ler.domain

import java.util.*

interface ScheduleNewPostsNotifUseCase {
    fun schedule(hour: Int, minute: Int)
}

typealias EnqueueLocalNotifAction = (Long) -> Unit
typealias CalendarProvider = () -> Calendar

class DefaultScheduleNewPostsNotifUseCase(
    val enqueueLocalNotifAction: EnqueueLocalNotifAction,
    val calendarProvider: CalendarProvider = { Calendar.getInstance(Locale.getDefault()) }
) : ScheduleNewPostsNotifUseCase {
    override fun schedule(hour: Int, minute: Int) {
        val datetimeToNotif = calendarProvider().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val currentDateTime = calendarProvider()
        if (currentDateTime > datetimeToNotif) {
            datetimeToNotif.add(Calendar.DAY_OF_MONTH, 1)
        }
        val delay = datetimeToNotif.timeInMillis - currentDateTime.timeInMillis
        enqueueLocalNotifAction(delay)
    }
}