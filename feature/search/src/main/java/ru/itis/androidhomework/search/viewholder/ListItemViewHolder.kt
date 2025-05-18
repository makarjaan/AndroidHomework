package ru.itis.androidhomework.search.viewholder

import androidx.recyclerview.widget.RecyclerView
import ru.itis.androidhomework.R
import ru.itis.androidhomework.databinding.ListItemBinding
import ru.itis.androidhomework.domain.model.FeaturesModel


class ListItemViewHolder(
    private val viewBinding: ListItemBinding,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.ViewHolder(viewBinding.root) {

    init {
        viewBinding.root.setOnClickListener {
            onItemClick.invoke(adapterPosition)
        }
    }

    fun bindItem(itemData: FeaturesModel) {
        with(viewBinding) {
            title.text = itemData.name
            rating.text = root.context.getString(R.string.rating, itemData.rate)
        }
    }
}