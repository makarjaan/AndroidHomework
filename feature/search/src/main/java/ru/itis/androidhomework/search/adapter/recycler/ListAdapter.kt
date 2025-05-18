package ru.itis.androidhomework.search.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.itis.androidhomework.databinding.ListItemBinding
import ru.itis.androidhomework.domain.model.FeaturesModel
import ru.itis.androidhomework.search.viewholder.ListItemViewHolder


class ListAdapter(
    private val onItemClick: (Int) -> Unit,
): RecyclerView.Adapter<ru.itis.androidhomework.search.viewholder.ListItemViewHolder>() {

    private val asyncListDiffer = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ru.itis.androidhomework.search.viewholder.ListItemViewHolder {
        val binding =
            ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ru.itis.androidhomework.search.viewholder.ListItemViewHolder(
            viewBinding = binding,
            onItemClick = onItemClick
        )
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    override fun onBindViewHolder(holder: ru.itis.androidhomework.search.viewholder.ListItemViewHolder, position: Int) {
        holder.bindItem(itemData = asyncListDiffer.currentList[position])
    }

    fun setData(newList: List<FeaturesModel>) {
        val t = newList.toList()
        asyncListDiffer.submitList(t)
    }

    fun getItemAt(position: Int): FeaturesModel? {
        return asyncListDiffer.currentList.getOrNull(position)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FeaturesModel>() {
            override fun areItemsTheSame(oldItem: FeaturesModel, newItem: FeaturesModel): Boolean {
                return oldItem.xid == newItem.xid
            }

            override fun areContentsTheSame(oldItem: FeaturesModel, newItem: FeaturesModel): Boolean {
                return oldItem == newItem
            }
        }
    }

}