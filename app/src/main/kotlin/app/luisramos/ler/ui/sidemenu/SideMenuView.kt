package app.luisramos.ler.ui.sidemenu

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.setPadding
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.luisramos.ler.BuildConfig
import app.luisramos.ler.R
import app.luisramos.ler.di.observe
import app.luisramos.ler.di.viewModels
import app.luisramos.ler.ui.views.UiState
import com.squareup.contour.ContourLayout

class SideMenuView(context: Context, attrs: AttributeSet? = null) : ContourLayout(context, attrs) {

    val textView = TextView(context).apply {
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
    private val accentView = View(context).apply {
        setBackgroundResource(R.color.colorAccent)
    }
    private val shadowView = View(context).apply {
        setBackgroundResource(R.drawable.shadow)
    }

    private val adapter get() = recyclerView.adapter as FeedAdapter
    val recyclerView = RecyclerView(context).apply {
        updatePadding(
            top = 8.dip,
            bottom = 8.dip
        )
        clipChildren = false
        clipToPadding = false

        adapter = FeedAdapter().apply {
            onItemClick = { viewModel.onItemTapped(it) }
        }
        layoutManager = LinearLayoutManager(context)
    }

    val versionTextView = TextView(context).apply {
        setTextAppearance(R.style.TextAppearance_MaterialComponents_Caption)
        setPadding(16.dip)
        setBackgroundResource(R.color.white)
        textAlignment = TEXT_ALIGNMENT_TEXT_END
    }

    private val viewModel: SideMenuViewModel by viewModels()

    init {
        setBackgroundResource(R.color.white)

        textView.layoutBy(
            x = matchParentX(),
            y = topTo { 0.ydip }
        )
        accentView.layoutBy(
            x = matchParentX(),
            y = bottomTo { textView.bottom() }.heightOf { 2.ydip }
        )
        recyclerView.layoutBy(
            x = matchParentX(),
            y = topTo { textView.bottom() }.bottomTo { parent.height() }
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

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        val versionText = "${BuildConfig.VERSION_NAME}.${BuildConfig.VERSION_CODE}"
        versionTextView.text = versionText

        viewModel.uiState.observe(this) {
            when (it) {
                is UiState.Error ->
                    Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
                is UiState.Success ->
                    adapter.submitList(it.data)
            }
        }
    }
}