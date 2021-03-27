package app.luisramos.ler.domain

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.util.*

class DefaultScheduleNewPostsNotifUseCaseTest {

    var delay = -1L
    val useCase = DefaultScheduleNewPostsNotifUseCase(
        enqueueLocalNotifAction = {
            delay = it
        }, calendarProvider = {
            Calendar.getInstance()
        })

    @Test
    fun `should schedule notification with correct delay`() {
        val calendar = Calendar.getInstance(Locale.ROOT)
        val hour = calendar.get(Calendar.HOUR_OF_DAY) + 2
        val minute = calendar.get(Calendar.MINUTE) + 15

        useCase.schedule(hour, minute)

        val expectedDelay: Long = 2 * 60 * 60 * 1000 + 15 * 60 * 1000
        val thirtySecondThreshold = 45000
        assertThat(delay).isIn(expectedDelay - thirtySecondThreshold until expectedDelay + thirtySecondThreshold)
    }
}