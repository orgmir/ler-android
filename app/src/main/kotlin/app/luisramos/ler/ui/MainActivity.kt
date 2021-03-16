package app.luisramos.ler.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.work.WorkInfo
import androidx.work.WorkManager
import app.luisramos.ler.App
import app.luisramos.ler.R
import app.luisramos.ler.di.observe
import app.luisramos.ler.domain.work.FeedUpdateWorker
import app.luisramos.ler.domain.work.UPDATE_WORK_ID
import app.luisramos.ler.ui.feeds.FeedListScreen
import app.luisramos.ler.ui.navigation.NavigatingActivity
import app.luisramos.ler.ui.navigation.Screen
import app.luisramos.ler.ui.navigation.activity
import app.luisramos.ler.ui.subscription.AddSubscriptionScreen

class MainActivity : NavigatingActivity() {

    val viewModel: ScaffoldViewModel by viewModels {
        val appContainer = (applicationContext as App).appContainer
        appContainer.activityViewModelProviderFactory
    }
    private val toolbar: Toolbar by lazy {
        findViewById(R.id.toolbar)
    }

    override fun getLauncherScreen(): Screen = FeedListScreen()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = ScaffoldView(this)
        setContentView(view)
        setupView(view)
        setupViewModel(view)

        installNavigation(savedInstanceState, view.container)

        if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            handleIntentSend()
        }
    }

    override fun onNewScreen(screen: Screen) {
        when (screen) {
            is FeedListScreen -> {
                viewModel.updateTitle(viewModel.selectedFeed.value ?: -1L)
                toolbar.setNavigationIcon(R.drawable.ic_menu)
            }
            else -> {
                title?.let { viewModel.updateTitle(it.toString()) }
                toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
            }
        }
    }

    private fun setupView(view: ScaffoldView) {
        setSupportActionBar(view.toolbarContainer.toolbar)

        view.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {
                view.toolbarContainer.toolbar.setNavigationOnClickListener {
                    when {
                        view.activity<NavigatingActivity>().backstack.size >= 1 ->
                            goBack()
                        else -> view.openDrawer()
                    }
                }
            }

            override fun onViewDetachedFromWindow(v: View?) {}
        })

        WorkManager.getInstance(this)
            .getWorkInfosForUniqueWorkLiveData(UPDATE_WORK_ID)
            .observe(view) {
                val workInfo = it.firstOrNull()
                val percent = when (workInfo?.state) {
                    WorkInfo.State.RUNNING -> workInfo.progress.getFloat(
                        FeedUpdateWorker.Progress,
                        0.10f
                    )
                    else -> 1f
                }

                view.toolbarContainer.toolbarProgress.animate()
                    .scaleX(percent)
                    .setDuration(150)
                    .start()
            }
    }

    private fun setupViewModel(view: ScaffoldView) {
        viewModel.selectedFeed.observe(this) {
            view.closeDrawer()
        }
        viewModel.title.observe(this) {
            title = it
        }
    }

    private fun handleIntentSend() {
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            val screen = AddSubscriptionScreen(it)
            resetTo(screen)
        }
    }
}

