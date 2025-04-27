package ru.itis.androidhomework.presentation.viewholder

import androidx.recyclerview.widget.RecyclerView
import ru.itis.androidhomework.R
import ru.itis.androidhomework.databinding.ListItemBinding
import ru.itis.androidhomework.presentation.model.ListItemData

class ListItemViewHolder(
    private val viewBinding: ListItemBinding,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.ViewHolder(viewBinding.root) {

    init {
        viewBinding.root.setOnClickListener {
            onItemClick.invoke(adapterPosition)
        }
    }

    fun bindItem(itemData: ListItemData) {
        with(viewBinding) {
            title.text = itemData.title
            kinds.text = itemData.categories
            rating.text = root.context.getString(R.string.rating, itemData.rating)
        }
    }
}