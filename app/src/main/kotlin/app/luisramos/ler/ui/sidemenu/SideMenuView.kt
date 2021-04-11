package app.luisramos.ler.ui.sidemenu

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.luisramos.ler.BuildConfig
import app.luisramos.ler.R
import app.luisramos.ler.ui.views.getDrawable
import com.squareup.contour.ContourLayout

class SideMenuView(
    context: Context,
    attrs: AttributeSet? = null
) : ContourLayout(context, attrs) {

    private val titleTextView = TextView(context).apply {
        updatePadding(
            left = 16.dip,
            right = 16.dip,
            top = 40.dip,
            bottom = 18.dip
        )
        setTextAppearance(R.style.TextAppearance_MaterialComponents_Headline6)
        setBackgroundResource(R.color.colorPrimary)
        text = resources.getString(R.string.app_name)
    }

    val addSubscriptionButton = Button(context).apply {
        background = getDrawable(R.attr.selectableItemBackground)
        isAllCaps = false
        textAlignment = TEXT_ALIGNMENT_TEXT_START
        setText(R.string.add_subscription)
        setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_add, 0)
        setPadding(16.dip)
    }

    private val accentView = View(context).apply {
        setBackgroundResource(R.color.colorAccent)
    }
    private val shadowView = View(context).apply {
        setBackgroundResource(R.drawable.shadow)
    }

    val adapter get() = recyclerView.adapter as FeedAdapter
    private val recyclerView = RecyclerView(context).apply {
        id = R.id.sideMenuRecyclerView
        updatePadding(
            top = 8.dip,
            bottom = 8.dip
        )
        clipChildren = false
        clipToPadding = false

        adapter = FeedAdapter()
        layoutManager = LinearLayoutManager(context)
    }

    private val versionTextView = TextView(context).apply {
        setTextAppearance(R.style.TextAppearance_MaterialComponents_Caption)
        setPadding(16.dip)
        setBackgroundResource(R.color.white)
        textAlignment = TEXT_ALIGNMENT_TEXT_END

        text = BuildConfig.VERSION_NAME
    }

    init {
        setBackgroundResource(R.color.white)

        titleTextView.layoutBy(
            x = leftTo { parent.left() },
            y = topTo { 0.ydip }
        )
        addSubscriptionButton.layoutBy(
            x = rightTo { parent.right() }
                .widthOf {
                    minOf(
                        addSubscriptionButton.preferredWidth(),
                        parent.width() - (titleTextView.right() + 4.xdip)
                    )
                },
            y = baselineTo { titleTextView.baseline() }
        )
        accentView.layoutBy(
            x = matchParentX(),
            y = bottomTo { titleTextView.bottom() }.heightOf { 2.ydip }
        )
        recyclerView.layoutBy(
            x = matchParentX(),
            y = topTo { titleTextView.bottom() }.bottomTo { parent.height() }
        )
        shadowView.layoutBy(
            x = matchParentX(),
            y = topTo { recyclerView.top() }
        )
        versionTextView.layoutBy(
            x = matchParentX(),
            y = bottomTo { parent.bottom() }
        )

        if (isInEditMode) {
            versionTextView.text = "1.0.1.10"
        }
    }
}