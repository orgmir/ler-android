package app.luisramos.ler.ui.feeds

import android.graphics.PorterDuff
import android.graphics.Typeface
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.luisramos.ler.R
import app.luisramos.ler.data.SelectAll
import java.text.SimpleDateFormat
import java.util.*


class FeedItemAdapter : ListAdapter<SelectAll, FeedItemAdapter.ViewHolder>(DiffUtilCallback()) {

    var onItemClick: ((Int) -> Unit)? = null

    private val dateFormatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FeedItemStackedView(parent.context))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.view.apply {
            textView1.text = item.title
            textView2.text = item.feedTitle
            textView3.text = item.publishedAt?.let { dateFormatter.format(it) } ?: "--"
            val textStyle = if (item.unread == true) {
                Typeface.BOLD
            } else {
                Typeface.NORMAL
            }
            textView1.setTextStyle(textStyle)
            val tintRes = if (item.unread == true) {
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
                onItemClick?.invoke(holder.bindingAdapterPosition)
            }
        }
    }

    class ViewHolder(val view: FeedItemStackedView) : RecyclerView.ViewHolder(view)

    class DiffUtilCallback : DiffUtil.ItemCallback<SelectAll>() {
        override fun areItemsTheSame(oldItem: SelectAll, newItem: SelectAll): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: SelectAll, newItem: SelectAll): Boolean =
            oldItem.title == newItem.title &&
                    oldItem.feedTitle == newItem.feedTitle &&
                    oldItem.publishedAt == newItem.publishedAt &&
                    oldItem.unread == newItem.unread
    }
}

fun TextView.setTextStyle(textStyle: Int) {
    if (textStyle == Typeface.NORMAL) {
        setTypeface(Typeface.create(typeface, Typeface.NORMAL), Typeface.NORMAL)
    } else {
        setTypeface(typeface, textStyle)
    }
}