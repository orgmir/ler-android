package app.luisramos.ler.ui.subscription

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class StackedLabelsAdapter : RecyclerView.Adapter<StackedLabelsAdapter.ViewHolder>() {

    var items: List<Pair<String, String>> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClick: ((Pair<String, String>) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = StackedLabelsView(parent.context)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val (text1, text2) = item
        (holder.itemView as StackedLabelsView).apply {
            textView1.text = text1
            textView2.text = text2
            setOnClickListener { onItemClick?.invoke(item) }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}