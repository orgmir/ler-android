package app.luisramos.ler.ui.subscription

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageButton
import androidx.core.view.setPadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.luisramos.ler.R
import app.luisramos.ler.ui.views.getDrawable
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.squareup.contour.ContourLayout

class AddSubscriptionView(
    context: Context,
    attrs: AttributeSet? = null
) : ContourLayout(context, attrs) {

    val editTextView = TextInputEditText(context).apply {
        id = R.id.editText
        imeOptions = EditorInfo.IME_ACTION_DONE
        inputType = InputType.TYPE_CLASS_TEXT
        maxLines = 1
    }
    val editTextInputLayout = TextInputLayout(context).apply {
        clipToPadding = false
        hint = resources.getString(R.string.hint_url)
        setPadding(16.dip)
    }

    val swipeRefreshLayout = SwipeRefreshLayout(context)

    val adapter get() = recyclerView.adapter as StackedLabelsAdapter
    val recyclerView = RecyclerView(context).apply {
        id = R.id.subscriptionRecyclerView
        layoutManager = LinearLayoutManager(context)
        adapter = StackedLabelsAdapter()
    }

    private val shadow = View(context).apply {
        setBackgroundResource(R.drawable.shadow)
    }

    val imageButton = ImageButton(context).apply {
        id = R.id.searchButton
        background = getDrawable(R.attr.selectableItemBackground)
        setImageResource(R.drawable.ic_baseline_search_24)
    }

    init {
        swipeRefreshLayout.layoutBy(
            x = matchParentX(),
            y = topTo { editTextInputLayout.bottom() }.bottomTo { parent.bottom() }
        )
        swipeRefreshLayout.addView(recyclerView)
        recyclerView.layoutBy(
            x = matchParentX(),
            y = matchParentY(),
            addToViewGroup = false
        )

        editTextInputLayout.layoutBy(
            x = leftTo { 0.xdip }.rightTo { imageButton.left() },
            y = topTo { 0.ydip }
        )
        editTextInputLayout.addView(editTextView)

        imageButton.layoutBy(
            x = rightTo { parent.width() }.widthOf { 44.xdip },
            y = centerVerticallyTo { editTextInputLayout.centerY() }.heightOf { 44.ydip }
        )

        shadow.layoutBy(
            x = matchParentX(),
            y = topTo { swipeRefreshLayout.top() }.heightOf { 4.ydip }
        )
    }
}
