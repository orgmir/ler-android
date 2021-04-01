package app.luisramos.ler.domain

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.util.*

class DefaultScheduleNewPostsNotifUseCaseTest {

    @Test
    fun `should schedule notification with correct delay`() {
        var delay = -1L
        val calendarProvider = {
            Calendar.getInstance(Locale.ROOT).apply {
                set(Calendar.YEAR, 1989)
                set(Calendar.MONTH, 11)
                set(Calendar.DAY_OF_MONTH, 16)
                set(Calendar.HOUR_OF_DAY, 11)
                set(Calendar.MINUTE, 30)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
        }
        val useCase = DefaultScheduleNewPostsNotifUseCase(
            enqueueLocalNotifAction = {
                delay = it
            }, calendarProvider = calendarProvider
        )
        val calendar = calendarProvider()
        val hour = calendar.get(Calendar.HOUR_OF_DAY) + 2
        val minute = calendar.get(Calendar.MINUTE) + 15

        useCase.schedule(hour, minute)

        val expectedDelay: Long = 2 * 60 * 60 * 1000 + 15 * 60 * 1000
        assertThat(delay).isEqualTo(expectedDelay)
    }
}