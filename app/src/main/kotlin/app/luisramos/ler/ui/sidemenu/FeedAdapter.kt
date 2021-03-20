package app.luisramos.ler.ui.sidemenu

import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.luisramos.ler.ui.feeds.setTextStyle
import app.luisramos.ler.ui.sidemenu.SideMenuViewModel.SideMenuItem

class FeedAdapter : ListAdapter<SideMenuItem, FeedAdapter.ViewHolder>(ItemDiffer()) {

    var onItemClick: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = FeedView(parent.context)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        (holder.itemView as FeedView).apply {
            textView1.text = item.title
            textView2.text = item.count

            val textStyle = if (item.isSelected) {
                Typeface.BOLD
            } else {
                Typeface.NORMAL
            }
            textView1.setTextStyle(textStyle)
            textView2.setTextStyle(textStyle)
            selectedAccentView.isVisible = item.isSelected

            setOnClickListener { onItemClick?.invoke(position) }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class ItemDiffer : DiffUtil.ItemCallback<SideMenuItem>() {
        override fun areItemsTheSame(oldItem: SideMenuItem, newItem: SideMenuItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: SideMenuItem, newItem: SideMenuItem): Boolean =
            oldItem == newItem
    }
}