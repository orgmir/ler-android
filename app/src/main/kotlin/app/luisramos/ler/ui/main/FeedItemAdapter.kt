package app.luisramos.ler.ui.main

import android.graphics.PorterDuff
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.luisramos.ler.R
import app.luisramos.ler.data.SelectAll
import app.luisramos.ler.data.toBoolean
import kotlinx.android.synthetic.main.item_feed.view.textView1
import kotlinx.android.synthetic.main.item_feed.view.textView2
import kotlinx.android.synthetic.main.item_feed_item_stacked.view.*
import java.text.SimpleDateFormat
import java.util.*


class FeedItemAdapter : RecyclerView.Adapter<FeedItemAdapter.ViewHolder>() {

    var items: List<SelectAll> = emptyList()
        set(value) {
            val oldItems = field
            field = value
            unreadMap = value.foldIndexed(mutableMapOf()) { index, acc, item ->
                acc[index] = item.unread.toBoolean()
                acc
            }
            val diff = DiffUtil.calculateDiff(DiffUtilCallback(oldItems, value), false)
            diff.dispatchUpdatesTo(this)
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
            val textStyle = if (item.unread.toBoolean()) {
                Typeface.BOLD
            } else {
                Typeface.NORMAL
            }
            textView1.setTextStyle(textStyle)
            val tintRes = if (item.unread.toBoolean()) {
                R.color.colorAccent
            } else {
                R.color.black_54
            }
            val tintColor = ContextCompat.getColor(context, tintRes)
            imageView.setColorFilter(tintColor, PorterDuff.Mode.MULTIPLY)

            setOnClickListener {
                // Since we are diffing the updates, if we use "position"
                // here it might be out of date. binding only happens for
                // new items
                onItemClick?.invoke(holder.adapterPosition)
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class DiffUtilCallback(
        private val oldData: List<SelectAll>,
        private val newData: List<SelectAll>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldData.size

        override fun getNewListSize(): Int = newData.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldData[oldItemPosition].id == newData[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val newItem = newData[newItemPosition]
            val oldItem = oldData[oldItemPosition]
            return oldItem.title == newItem.title &&
                    oldItem.feedTitle == newItem.feedTitle &&
                    oldItem.publishedAt == newItem.publishedAt &&
                    oldItem.unread == newItem.unread
        }
    }
}

fun TextView.setTextStyle(textStyle: Int) {
    if (textStyle == Typeface.NORMAL) {
        setTypeface(Typeface.create(typeface, Typeface.NORMAL), Typeface.NORMAL)
    } else {
        setTypeface(typeface, textStyle)
    }
}