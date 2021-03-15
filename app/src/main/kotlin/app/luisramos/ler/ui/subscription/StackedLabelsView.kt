package app.luisramos.ler.ui.subscription

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.luisramos.ler.R
import app.luisramos.ler.ui.views.getDrawable
import com.squareup.contour.ContourLayout

class StackedLabelsView(context: Context, attrs: AttributeSet? = null) :
    ContourLayout(context, attrs) {

    val imageView = ImageView(context).apply {
        setImageResource(R.drawable.ic_baseline_rss_feed_24)
    }

    val textView1 = TextView(context).apply {
        setTextAppearance(R.style.TextAppearance_MaterialComponents_Body1)
    }

    val textView2 = TextView(context).apply {
        setTextAppearance(R.style.TextAppearance_MaterialComponents_Body2)
        alpha = 0.87f
    }

    private val separator = View(context).apply {
        setBackgroundResource(R.color.separator)
    }

    init {
        background = getDrawable(R.attr.selectableItemBackground)

        imageView.layoutBy(
            x = leftTo { parent.left() + 16.xdip },
            y = centerVerticallyTo { parent.centerY() }
        )
        textView1.layoutBy(
            x = leftTo { imageView.right() + 16.xdip }.rightTo { parent.right() - 16.xdip },
            y = topTo { 16.ydip }
        )
        textView2.layoutBy(
            x = leftTo { textView1.left() }.rightTo { parent.right() - 16.xdip },
            y = topTo { textView1.bottom() + 4.ydip }
        )
        separator.layoutBy(
            x = leftTo { 16.xdip }.rightTo { parent.right() },
            y = bottomTo { parent.bottom() }.heightOf { 2.ydip }
        )
        contourHeightOf {
            textView2.bottom() + 16.ydip
        }

        if (isInEditMode) {
            textView1.text = "The Working Directory"
            textView2.text = "http://example.com"
        }
    }
}