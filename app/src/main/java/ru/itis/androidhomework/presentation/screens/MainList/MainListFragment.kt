package ru.itis.androidhomework.presentation.screens.MainList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.itis.androidhomework.R
import ru.itis.androidhomework.databinding.FragmentMainListBinding
import ru.itis.androidhomework.presentation.adapter.recycler.ListAdapter
import ru.itis.androidhomework.presentation.model.ListItemData


class MainListFragment : Fragment(R.layout.fragment_main_list) {

    private var viewBinding: FragmentMainListBinding? = null

    private var rvAdapter: ListAdapter? = null

    private var currentDataList = mutableListOf<ListItemData>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMainListBinding.inflate(layoutInflater)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    fun initView() {
        rvAdapter = ListAdapter(
            onItemClick = :: onItemClick,
            items = currentDataList
        )
    }

    fun onItemClick(position: Int) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    companion object {
        const val TAG = "MainListFragment"
    }
}