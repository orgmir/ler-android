package app.luisramos.ler.ui.settings

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.updatePadding
import app.luisramos.ler.R
import com.google.android.material.switchmaterial.SwitchMaterial
import com.squareup.contour.ContourLayout

class SettingsItemSwitchView(
    context: Context,
    attrs: AttributeSet? = null
) : ContourLayout(context, attrs) {

    var clickListener = {}

    val titleTextView = TextView(context).apply {
        setTextAppearance(R.style.TextAppearance_MaterialComponents_Body1)
        gravity = Gravity.CENTER_VERTICAL
        updatePadding(left = 16.dip, right = 16.dip, top = 16.dip, bottom = 4.dip)
        isClickable = false
    }

    val subTitleTextView = TextView(context).apply {
        setTextAppearance(R.style.TextAppearance_MaterialComponents_Body2)
        gravity = Gravity.CENTER_VERTICAL
        updatePadding(left = 16.dip, right = 16.dip, top = 4.dip, bottom = 16.dip)
        setOnClickListener { switch.performClick() }
        visibility = GONE
        isClickable = false
    }

    val switch = SwitchMaterial(context).apply {
        setOnClickListener { clickListener() }
    }

    init {
        setBackgroundResource(R.drawable.background_item_with_divider)
        setOnClickListener { switch.performClick() }

        switch.layoutBy(
            x = rightTo { parent.width() - 16.xdip },
            y = centerVerticallyTo { parent.centerY() }
        )
        titleTextView.layoutBy(
            x = leftTo { 0.xdip }.rightTo { switch.left() },
            y = topTo { 0.ydip }.heightOf { titleTextView.preferredHeight() }
        )
        subTitleTextView.layoutBy(
            x = matchXTo(titleTextView),
            y = topTo { titleTextView.bottom() }.heightOf { subTitleTextView.preferredHeight() }
        )
        contourHeightOf {
            if (subTitleTextView.isGone) {
                titleTextView.bottom() + 12.dip
            } else {
                subTitleTextView.bottom()
            }
        }
    }
}

