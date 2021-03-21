package app.luisramos.ler.domain

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class DefaultSaveNotifyTimePrefUseCaseTest {
    private val prefs = FakePreferences()
    private val useCase = DefaultSaveNotifyTimePrefUseCase(prefs)

    @Test
    fun `getting time should return correct hour and minute`() {
        prefs.newPostNotificationTime = 930

        val (hour, minutes) = useCase.notifyHourMinute

        assertThat(hour).isEqualTo(9)
        assertThat(minutes).isEqualTo(30)
    }

    @Test
    fun `saving time should save it correctly in an integer`() = runBlockingTest {
        useCase.savePref(hour = 11, minute = 45)

        assertThat(prefs.newPostNotificationTime).isEqualTo(1145)
    }
}