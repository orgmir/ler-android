package app.luisramos.ler.ui.feeds

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.luisramos.ler.R
import com.squareup.contour.ContourLayout

class FeedItemsListView(
    context: Context,
    attrs: AttributeSet? = null
) : ContourLayout(context, attrs) {

    val swipeRefreshLayout = SwipeRefreshLayout(context)

    val adapter get() = recyclerView.adapter as FeedItemAdapter
    val recyclerView = RecyclerView(context).apply {
        id = R.id.feedItemsRecyclerView
        setBackgroundResource(R.color.swipe_action_background)
        layoutManager = LinearLayoutManager(context)
        this.adapter = FeedItemAdapter()
    }

    val emptyView = ImageView(context).apply {
        visibility = View.GONE
        alpha = 0.2f
        setImageResource(R.drawable.ic_baseline_rss_feed_24)
    }

    init {
        setBackgroundResource(R.color.white)

        swipeRefreshLayout.layoutBy(
            x = matchParentX(),
            y = matchParentY()
        )
        swipeRefreshLayout.addView(recyclerView)
        recyclerView.layoutBy(
            x = matchParentX(),
            y = matchParentY(),
            addToViewGroup = false
        )
        emptyView.layoutBy(
            x = centerHorizontallyTo { parent.centerX() }.widthOf { 80.xdip },
            y = centerVerticallyTo { parent.centerY() }.heightOf { 80.ydip }
        )
    }
}