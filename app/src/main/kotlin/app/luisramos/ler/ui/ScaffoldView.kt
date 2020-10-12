package app.luisramos.ler.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.transition.TransitionManager
import app.luisramos.ler.R
import app.luisramos.ler.ui.sidemenu.SideMenuScreen
import app.luisramos.ler.ui.views.ToolbarView
import com.squareup.contour.ContourLayout

class ScaffoldView(context: Context, attrs: AttributeSet? = null) : ContourLayout(context, attrs) {

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
        setOnClickListener { /* intercept touches */ }
    }

    val container = FrameLayout(context)

    // LR: Bit of a uglyness, but keeps SideMenuView cleaner
    private val sideMenuScreen = SideMenuScreen()
    private val sideMenuView = sideMenuScreen.createView(this)

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
    }

    fun openDrawer() {
        isDrawerOpen = true
    }

    fun closeDrawer() {
        isDrawerOpen = false
    }
}