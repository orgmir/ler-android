package app.luisramos.ler.ui.screen

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.test.core.app.launchActivity
import app.luisramos.ler.di.viewModels
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class NavigatingActivityTest {
    @Test
    fun whenScreenIsDismissed_viewModelShouldBeCleared() {
        val lastScreen = TestScreen("Another one")
        val scenario = launchActivity<TestActivity>()
        scenario.onActivity { activity ->
            activity.goTo(lastScreen)
            assertThat(activity.initialScreen.testViewModel.wasCleared).isTrue()
        }
        scenario.moveToState(Lifecycle.State.DESTROYED)
        assertThat(lastScreen.testViewModel.wasCleared).isTrue()
    }

    class TestActivity : NavigatingActivity() {
        val initialScreen = TestScreen("Hello there")
        override fun getLauncherScreen(): Screen = initialScreen
    }

    class TestScreen(val title: String) : Screen() {
        lateinit var testViewModel: TestViewModel
        override fun createView(container: ViewGroup): View =
            TextView(container.context).apply {
                val viewModel: TestViewModel by viewModels()
                testViewModel = viewModel
                text = title
            }
    }

    class TestViewModel : ViewModel() {
        var wasCleared = false
        override fun onCleared() {
            wasCleared = true
            super.onCleared()
        }
    }
}