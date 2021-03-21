package app.luisramos.ler.ui.screen

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.launchActivity
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class NavigatingActivityTest {
    @Test
    fun whenScreenIsDismissed_viewModelsShouldBeCleared() {
        val lastScreen = TestScreen("Another one")
        var initialScreen: TestScreen? = null
        launchActivity<NavigatingTestActivity>().use { scenario ->
            scenario.onActivity { activity ->
                activity.goTo(lastScreen)
                initialScreen = activity.initialScreen
                assertThat(activity.initialScreen.testViewModel.wasCleared).isFalse()
            }
            scenario.moveToState(Lifecycle.State.DESTROYED)
            assertThat(initialScreen?.testViewModel?.wasCleared).isTrue()
            assertThat(lastScreen.testViewModel.wasCleared).isTrue()
        }
    }
}