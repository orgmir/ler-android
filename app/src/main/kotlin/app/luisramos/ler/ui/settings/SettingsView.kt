package app.luisramos.ler.ui.settings

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.luisramos.ler.R
import com.squareup.contour.ContourLayout

class SettingsView(
    context: Context,
    attrs: AttributeSet? = null
) : ContourLayout(context, attrs) {

    val recyclerView = RecyclerView(context).apply {
        layoutManager = LinearLayoutManager(context)
        adapter = SettingsAdapter()
    }
    val adapter get() = recyclerView.adapter as SettingsAdapter

    init {
        setBackgroundResource(R.color.white)
        recyclerView.layoutBy(x = matchParentX(), y = matchParentY())
    }
}