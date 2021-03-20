package app.luisramos.ler.ui.feeds

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.luisramos.ler.R
import com.squareup.contour.ContourLayout

class FeedItemStackedView(
    context: Context,
    attrs: AttributeSet? = null
) : ContourLayout(context, attrs) {
    val imageView = ImageView(context).apply {
        setImageResource(R.drawable.ic_baseline_rss_feed_24)
        contentDescription = context.getString(R.string.content_desc_blog)
    }

    val textView1 = TextView(context).apply {
        setTextAppearance(R.style.TextAppearance_MaterialComponents_Body1)
        ellipsize = TextUtils.TruncateAt.END
        maxLines = 2
    }

    val textView2 = TextView(context).apply {
        setTextAppearance(R.style.TextAppearance_AppCompat_Body2)
        ellipsize = TextUtils.TruncateAt.END
        alpha = 0.8f
        maxLines = 1
    }

    val textView3 = TextView(context).apply {
        setTextAppearance(R.style.TextAppearance_MaterialComponents_Caption)
        maxLines = 1
        ellipsize = TextUtils.TruncateAt.END
    }

    private val bottomSeparator1 = View(context).apply {
        setBackgroundColor(Color.parseColor("#0F000000"))
    }
    private val bottomSeparator2 = View(context).apply {
        setBackgroundColor(Color.parseColor("#08000000"))
    }

    init {
        setBackgroundResource(R.drawable.background_selector_white)

        contourHeightOf {
            textView1.bottom() + 16.ydip
        }
        imageView.layoutBy(
            x = leftTo { 0.xdip }.rightTo { textView1.left() },
            y = centerVerticallyTo { parent.centerY() }
        )
        textView1.layoutBy(
            x = matchParentX(56.dip, 16.dip),
            y = topTo { textView2.bottom() + 4.ydip }
        )
        textView2.layoutBy(
            x = leftTo { textView1.left() }.rightTo { textView3.left() - 8.xdip },
            y = topTo { 8.ydip }
        )
        textView3.layoutBy(
            x = rightTo { textView1.right() },
            y = baselineTo { textView2.baseline() }
        )
        bottomSeparator1.layoutBy(
            x = matchParentX(),
            y = bottomTo { bottomSeparator2.top() }.heightOf { 1.ydip }
        )
        bottomSeparator2.layoutBy(
            x = matchParentX(),
            y = bottomTo { parent.bottom() }.heightOf { 1.ydip }
        )

        @SuppressLint("SetTextI18n")
        if (isInEditMode) {
            textView1.text =
                "Understanding Bits, Bytes, Bases, and Writing a Hex Dump in Javascript (Node)"
            textView2.text = "Tania Rascia"
            textView3.text = "13 January 2020"
        }
    }
}