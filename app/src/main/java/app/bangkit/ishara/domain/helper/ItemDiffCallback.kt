package app.bangkit.ishara.domain.helper

import androidx.recyclerview.widget.DiffUtil
import app.bangkit.ishara.data.models.Item

class ItemDiffCallback : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        // Assuming each item has a unique ID
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        // Check for content equality
        return oldItem == newItem
    }
}
