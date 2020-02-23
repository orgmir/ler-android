package app.luisramos.thecollector.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.luisramos.thecollector.R
import app.luisramos.thecollector.data.Feed
import kotlinx.android.synthetic.main.item_feed.view.*

class FeedAdapter : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

    var items: List<Feed> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_feed, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.itemView.apply {
            textView1.text = item.title
            textView2.text = "Some random description from the blog"
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}