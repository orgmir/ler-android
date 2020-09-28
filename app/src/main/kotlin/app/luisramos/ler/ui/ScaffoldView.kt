package app.luisramos.ler.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.transition.TransitionManager
import androidx.work.WorkInfo
import androidx.work.WorkManager
import app.luisramos.ler.App
import app.luisramos.ler.R
import app.luisramos.ler.UPDATE_WORK_ID
import app.luisramos.ler.di.observe
import app.luisramos.ler.domain.work.FeedUpdateWorker
import app.luisramos.ler.ui.navigation.NavigatingActivity
import app.luisramos.ler.ui.navigation.activity
import app.luisramos.ler.ui.navigation.goBack
import app.luisramos.ler.ui.sidemenu.SideMenuView
import app.luisramos.ler.ui.views.ToolbarView
import com.squareup.contour.ContourLayout

class ScaffoldView(context: Context, attrs: AttributeSet? = null) : ContourLayout(context, attrs) {

    val viewModel: ScaffoldViewModel by (context as ComponentActivity).viewModels {
        val appContainer = (context.applicationContext as App).appContainer
        appContainer.activityViewModelProviderFactory
    }

    var isDrawerOpen = false
        set(value) {
            TransitionManager.beginDelayedTransition(this)
            touchArea.animate()
                .alpha(if (value) 1f else 0f)
                .start()
            touchArea.isClickable = value
            field = value
            requestLayout()
        }

    val toolbarContainer = ToolbarView(context).apply {
        toolbarProgress.pivotX = 0f
        WorkManager.getInstance(context)
            .getWorkInfosForUniqueWorkLiveData(UPDATE_WORK_ID)
            .observe(this) {
                val workInfo = it.firstOrNull()
                val percent = when (workInfo?.state) {
                    WorkInfo.State.RUNNING -> workInfo.progress.getFloat(
                        FeedUpdateWorker.Progress,
                        0.10f
                    )
                    else -> 1f
                }

                toolbarProgress.animate()
                    .scaleX(percent)
                    .setDuration(150)
                    .start()
            }
        setOnClickListener { /* intercept touches */ }
    }

    val container = FrameLayout(context)
    val sideMenuView = SideMenuView(context)

    val touchArea = View(context).apply {
        setBackgroundResource(R.color.side_menu_background)
        alpha = 0f
        setOnClickListener { closeDrawer() }
    }

    init {
        id = R.id.scaffoldView
        fitsSystemWindows = true

        toolbarContainer.layoutBy(
            x = matchParentX(),
            y = topTo { 0.ydip }
        )
        container.layoutBy(
            x = matchParentX(),
            y = topTo { toolbarContainer.bottom() }.bottomTo { parent.bottom() }
        )
        sideMenuView.layoutBy(
            x = leftTo { if (isDrawerOpen) 0.xdip else (-340).xdip }
//            x = leftTo { 0.xdip }
                .widthOf { 340.xdip },
            y = matchParentY()
        )
        touchArea.layoutBy(
            x = leftTo { sideMenuView.right() }.rightTo { parent.width() },
            y = matchParentY()
        )

        setupViewModel()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        toolbarContainer.toolbar.setNavigationOnClickListener {
            when {
                activity<NavigatingActivity>().backstack.size >= 1 ->
                    goBack()
                else -> openDrawer()
            }
        }
    }

    fun openDrawer() {
        isDrawerOpen = true
    }

    fun closeDrawer() {
        isDrawerOpen = false
    }

    private fun setupViewModel() {
        viewModel.selectedFeed.observe(this) {
            closeDrawer()
        }
        viewModel.title.observe(this) {
            activity.title = it
        }
    }
}