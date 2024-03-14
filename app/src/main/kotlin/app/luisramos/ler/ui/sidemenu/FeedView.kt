package app.luisramos.ler.ui.sidemenu

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.luisramos.ler.R
import app.luisramos.ler.ui.views.getDrawable
import com.squareup.contour.ContourLayout

class FeedView(context: Context, attrs: AttributeSet? = null) : ContourLayout(context, attrs) {

    val selectedAccentView = View(context).apply {
        setBackgroundResource(R.color.colorAccent)
    }

    val imageView = ImageView(context).apply {
        contentDescription = resources.getString(R.string.content_desc_blog)
        setImageResource(R.drawable.ic_baseline_rss_feed_24)
    }

    val textView1 = TextView(context).apply {
        setTextAppearance(com.google.android.material.R.style.TextAppearance_MaterialComponents_Body1)
        maxLines = 3
    }

    val textView2 = TextView(context).apply {
        setTextAppearance(com.google.android.material.R.style.TextAppearance_MaterialComponents_Body1)
        maxLines = 1
        textAlignment = TEXT_ALIGNMENT_CENTER
    }

    init {
        clipToPadding = false
        val background = getDrawable(androidx.appcompat.R.attr.selectableItemBackground)
        setBackground(background)

        selectedAccentView.layoutBy(
            x = leftTo { parent.left() }.widthOf { 4.xdip },
            y = matchYTo(textView1, (-16).dip, (-16).dip)
        )
        imageView.layoutBy(
            x = leftTo { 16.xdip },
            y = centerVerticallyTo { textView1.centerY() }
        )
        textView1.layoutBy(
            x = leftTo { imageView.right() + 16.xdip }.rightTo { textView2.left() - 8.xdip },
            y = topTo { 16.ydip }
        )
        textView2.layoutBy(
            x = rightTo { parent.right() - 16.xdip },
            y = centerVerticallyTo { textView1.centerY() }
        )
        contourHeightOf {
            textView1.bottom() + 16.ydip
        }

        if (isInEditMode) {
            textView1.text = "Docker Images: Part I - Reducing Image size"
            textView2.text = "423"
        }
    }
}