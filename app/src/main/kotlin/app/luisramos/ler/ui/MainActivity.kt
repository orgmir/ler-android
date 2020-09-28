package app.luisramos.ler.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import app.luisramos.ler.R
import app.luisramos.ler.ui.feeds.FeedListScreen
import app.luisramos.ler.ui.navigation.NavigatingActivity
import app.luisramos.ler.ui.navigation.Screen
import app.luisramos.ler.ui.subscription.AddSubscriptionScreen

class MainActivity : NavigatingActivity() {

    private val scaffoldViewModel by lazy {
        findViewById<ScaffoldView>(R.id.scaffoldView).viewModel
    }
    private val toolbar: Toolbar by lazy {
        findViewById(R.id.toolbar)
    }

    override fun getLauncherScreen(): Screen = FeedListScreen()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = ScaffoldView(this)
        setContentView(view)
        setSupportActionBar(view.toolbarContainer.toolbar)

        installNavigation(savedInstanceState, view.container)

        if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            handleIntentSend()
        }
    }

    override fun onNewScreen(screen: Screen) {
        when (screen) {
            is FeedListScreen -> {
                scaffoldViewModel.updateTitle(scaffoldViewModel.selectedFeed.value ?: -1L)
                toolbar.setNavigationIcon(R.drawable.ic_menu)
            }
            else -> {
                title?.let { scaffoldViewModel.updateTitle(it.toString()) }
                toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
            }
        }
    }

    private fun handleIntentSend() {
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            val screen = AddSubscriptionScreen(it)
            resetTo(screen)
        }
    }
}

