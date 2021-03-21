package app.luisramos.ler.ui.screen

import android.os.Bundle
import android.widget.FrameLayout

class NavigatingTestActivity : NavigatingActivity() {
    val initialScreen = TestScreen("Hello there")
    override fun getLauncherScreen(): Screen = initialScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = FrameLayout(this)
        setContentView(view)
        installNavigation(savedInstanceState, view)
    }
}