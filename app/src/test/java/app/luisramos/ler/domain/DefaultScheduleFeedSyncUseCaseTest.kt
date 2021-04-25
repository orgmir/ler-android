package app.luisramos.ler.domain

import app.luisramos.ler.test.FakePreferences
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DefaultScheduleFeedSyncUseCaseTest {

    private val fakePrefs = FakePreferences()
    private var didCallEnqueueFeedSync = false
    private val enqueueFeedSyncWork = { didCallEnqueueFeedSync = true }
    private val useCase = DefaultScheduleFeedSyncUseCase(fakePrefs, enqueueFeedSyncWork)

    @Test
    fun `given feed refresh is enabled, when useCase is called, refresh feeds`() {
        fakePrefs.isFeedRefreshEnabled = true
        useCase.schedule()
        assertThat(didCallEnqueueFeedSync).isTrue()
    }

    @Test
    fun `given feed refresh is disabled, when useCase is called, refresh feeds`() {
        fakePrefs.isFeedRefreshEnabled = false
        useCase.schedule()
        assertThat(didCallEnqueueFeedSync).isFalse()
    }
}