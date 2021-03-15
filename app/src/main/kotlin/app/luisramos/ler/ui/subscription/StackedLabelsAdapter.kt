package app.luisramos.ler.ui.subscription

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.luisramos.ler.ui.subscription.AddSubscriptionViewModel.SubscriptionUiModel

class StackedLabelsAdapter :
    ListAdapter<SubscriptionUiModel, StackedLabelsAdapter.ViewHolder>(ItemCallback()) {

    var onItemClick: (Int) -> Unit = { }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = StackedLabelsView(parent.context)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val (text1, text2) = item
        (holder.itemView as StackedLabelsView).apply {
            textView1.text = text1
            textView2.text = text2
            setOnClickListener { onItemClick.invoke(holder.bindingAdapterPosition) }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class ItemCallback : DiffUtil.ItemCallback<SubscriptionUiModel>() {
        override fun areItemsTheSame(
            oldItem: SubscriptionUiModel,
            newItem: SubscriptionUiModel
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: SubscriptionUiModel,
            newItem: SubscriptionUiModel
        ): Boolean = oldItem == newItem
    }
}