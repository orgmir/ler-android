package app.luisramos.ler.domain

import app.luisramos.ler.test.FakePreferences
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class DefaultNewPostsNotificationPreferencesUseCaseTest {
    private val prefs = FakePreferences()
    private val scheduleUseCase = FakeScheduleNewPostsNotifUseCase()
    private val cancelUseCase = FakeCancelNewPostsNotificationUseCase()
    private val useCase =
        DefaultNewPostsNotificationPreferencesUseCase(scheduleUseCase, cancelUseCase, prefs)

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
        assertThat(scheduleUseCase.scheduleWasCalledWithHourMinute).isEqualTo(11 to 45)
    }

    @Test
    fun `enabling notification should schedule it`() {
        useCase.isNotificationEnabled = true

        assertThat(prefs.isNewPostNotificationEnabled).isTrue()
        assertThat(scheduleUseCase.scheduleWasCalledWithHourMinute).isEqualTo(8 to 0)
        assertThat(cancelUseCase.didCallCancelScheduledNotification).isFalse()
    }

    @Test
    fun `disabling notification should cancel it`() {
        useCase.isNotificationEnabled = false

        assertThat(prefs.isNewPostNotificationEnabled).isFalse()
        assertThat(scheduleUseCase.scheduleWasCalledWithHourMinute).isNull()
        assertThat(cancelUseCase.didCallCancelScheduledNotification).isTrue()
    }
}

class FakeScheduleNewPostsNotifUseCase : ScheduleNewPostsNotifUseCase {
    var scheduleWasCalledWithHourMinute: Pair<Int, Int>? = null
    override fun schedule(hour: Int, minute: Int) {
        scheduleWasCalledWithHourMinute = hour to minute
    }
}

class FakeCancelNewPostsNotificationUseCase : CancelNewPostsNotificationUseCase {
    var didCallCancelScheduledNotification = false
    override fun cancelScheduledNotification() {
        didCallCancelScheduledNotification = true
    }
}