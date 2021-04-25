package app.luisramos.ler.ui.settings

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class SettingsAdapter : ListAdapter<SettingsUiModel, SettingsAdapter.ViewHolder>(ItemCallback()) {

    var itemClickListener: (Int) -> Unit = {}

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is SettingsUiModel.Switch -> 0
        is SettingsUiModel.TimePicker -> 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            1 -> ViewHolder(SettingsItemTimePickerView(parent.context))
            else -> ViewHolder(SettingsItemSwitchView(parent.context))
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val itemView = holder.itemView
        when {
            itemView is SettingsItemSwitchView && item is SettingsUiModel.Switch -> itemView.apply {
                titleTextView.setText(item.title)
                item.desc?.let { subTitleTextView.setText(it) }
                subTitleTextView.isVisible = item.desc != null
                switch.isChecked = item.isChecked
                clickListener = { itemClickListener(position) }
            }
            itemView is SettingsItemTimePickerView && item is SettingsUiModel.TimePicker ->
                itemView.apply {
                    titleTextView.setText(item.title)
                    subTitleTextView.setText(item.desc)
                    timeTextView.text = item.time
                    clickListener = { itemClickListener(position) }
                }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class ItemCallback : DiffUtil.ItemCallback<SettingsUiModel>() {
        override fun areItemsTheSame(oldItem: SettingsUiModel, newItem: SettingsUiModel): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(
            oldItem: SettingsUiModel,
            newItem: SettingsUiModel
        ): Boolean = oldItem == newItem
    }
}