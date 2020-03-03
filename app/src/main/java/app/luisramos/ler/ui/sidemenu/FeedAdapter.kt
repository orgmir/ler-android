package app.luisramos.ler.ui.sidemenu

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.luisramos.ler.R
import app.luisramos.ler.ui.main.setTextStyle
import app.luisramos.ler.ui.main.toggleGone
import app.luisramos.ler.ui.sidemenu.SideMenuViewModel.SideMenuItem
import kotlinx.android.synthetic.main.item_feed.view.*

class FeedAdapter : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

    var selectedFeed = -1L
        set(value) {
            val oldPos = items.indexOfFirst { it.id == field }
            field = value

            val newPos = items.indexOfFirst { it.id == field }
            if (oldPos != -1) {
                notifyItemChanged(oldPos)
            }
            if (newPos != -1) {
                notifyItemChanged(newPos)
            }
        }
    var items: List<SideMenuItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClick: ((Int) -> Unit)? = null

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
            textView2.text = item.count

            val textStyle = if (selectedFeed == item.id) {
                Typeface.BOLD
            } else {
                Typeface.NORMAL
            }
            textView1.setTextStyle(textStyle)
            textView2.setTextStyle(textStyle)
            selectedAccentView.visibility = (selectedFeed == item.id).toggleGone()

            setOnClickListener { onItemClick?.invoke(position) }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}