package app.luisramos.ler.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.Toolbar
import app.luisramos.ler.R
import com.squareup.contour.ContourLayout

class ToolbarView(context: Context, attrs: AttributeSet? = null) : ContourLayout(context, attrs) {
    val toolbar =
        Toolbar(context).apply {
            id = R.id.toolbar
            getContext().setTheme(android.R.style.ThemeOverlay_Material_ActionBar)
            setNavigationIcon(R.drawable.ic_menu)
        }

    val toolbarProgress = View(context).apply {
        setBackgroundResource(R.color.colorAccent)
    }

    init {
        contourHeightWrapContent()
        val toolbarHeight = getDimen(android.R.attr.actionBarSize).toInt()
        toolbar.layoutBy(
            x = matchParentX(),
            y = topTo { 0.ydip }.heightOf { toolbarHeight.toYInt() }
        )
        toolbarProgress.layoutBy(
            x = leftTo { 0.xdip }.widthOf { parent.width() },
            y = topTo { toolbar.bottom() }.heightOf { 2.ydip }
        )
    }
}