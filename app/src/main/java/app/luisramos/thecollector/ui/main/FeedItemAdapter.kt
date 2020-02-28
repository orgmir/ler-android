package app.luisramos.thecollector.ui.main

import android.graphics.PorterDuff
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.luisramos.thecollector.R
import app.luisramos.thecollector.data.SelectAll
import kotlinx.android.synthetic.main.item_feed.view.textView1
import kotlinx.android.synthetic.main.item_feed.view.textView2
import kotlinx.android.synthetic.main.item_feed_item_stacked.view.*
import java.text.SimpleDateFormat
import java.util.*


class FeedItemAdapter : RecyclerView.Adapter<FeedItemAdapter.ViewHolder>() {

    var items: List<SelectAll> = emptyList()
        set(value) {
            field = value
            unreadMap = value.foldIndexed(mutableMapOf()) { index, acc, item ->
                acc[index] = item.unread
                acc
            }
            notifyDataSetChanged()
        }

    var unreadMap = mutableMapOf<Int, Boolean>()

    var onItemClick: ((Int) -> Unit)? = null

    private val dateFormatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_feed_item_stacked, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.itemView.apply {
            textView1.text = item.title
            textView2.text = item.feedTitle
            textView3.text = item.publishedAt?.let { dateFormatter.format(it) } ?: "--"
            val textStyle = if (item.unread) {
                Typeface.BOLD
            } else {
                Typeface.NORMAL
            }
            textView1.setTextStyle(textStyle)
            val tintRes = if (item.unread) {
                R.color.colorAccent
            } else {
                R.color.black_54
            }
            val tintColor = ContextCompat.getColor(context, tintRes)
            imageView.setColorFilter(tintColor, PorterDuff.Mode.MULTIPLY)

            setOnClickListener {
                onItemClick?.invoke(position)
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

fun TextView.setTextStyle(textStyle: Int) {
    if (textStyle == Typeface.NORMAL) {
        setTypeface(Typeface.create(typeface, Typeface.NORMAL), Typeface.NORMAL)
    } else {
        setTypeface(typeface, textStyle)
    }
}