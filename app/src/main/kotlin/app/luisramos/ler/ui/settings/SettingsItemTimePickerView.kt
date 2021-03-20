package app.luisramos.ler.ui.settings

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.updatePadding
import app.luisramos.ler.R
import com.squareup.contour.ContourLayout

class SettingsItemTimePickerView(
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
        setTextAppearance(R.style.TextAppearance_MaterialComponents_Caption)
        gravity = Gravity.CENTER_VERTICAL
        updatePadding(left = 16.dip, right = 16.dip, top = 4.dip, bottom = 16.dip)
        isClickable = false
    }

    val timeTextView = TextView(context).apply {
        setTextAppearance(R.style.TextAppearance_MaterialComponents_Body1)
        gravity = Gravity.CENTER
        isClickable = false
    }

    init {
        setBackgroundResource(R.drawable.background_item_with_divider)
        setOnClickListener { clickListener() }

        timeTextView.layoutBy(
            x = rightTo { parent.width() - 16.xdip },
            y = centerVerticallyTo { parent.centerY() }
        )
        titleTextView.layoutBy(
            x = leftTo { 0.xdip }.rightTo { timeTextView.left() },
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

        if (isInEditMode) {
            timeTextView.text = "08:35"
            titleTextView.text = "\"New posts\" notification time"
            subTitleTextView.text =
                "The time the daily notification will trigger letting you know of new posts from your subscriptions."
        }
    }
}