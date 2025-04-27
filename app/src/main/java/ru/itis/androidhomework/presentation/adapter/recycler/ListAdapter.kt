package ru.itis.androidhomework.presentation.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.itis.androidhomework.databinding.ListItemBinding
import ru.itis.androidhomework.presentation.model.ListItemData
import ru.itis.androidhomework.presentation.viewholder.ListItemViewHolder

class ListAdapter(
    private val onItemClick: (Int) ->Unit,
    items: List<ListItemData>
): RecyclerView.Adapter<ListItemViewHolder>() {

    private var dataList = mutableListOf<ListItemData>()

    init {
        dataList.addAll(items)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val binding =
            ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListItemViewHolder(
            viewBinding = binding,
            onItemClick = onItemClick
        )
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        holder.bindItem(itemData = dataList[position])
    }

}