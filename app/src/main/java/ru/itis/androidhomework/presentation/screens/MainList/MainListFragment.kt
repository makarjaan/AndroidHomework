package ru.itis.androidhomework.presentation.screens.MainList

import  android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import ru.itis.androidhomework.R
import ru.itis.androidhomework.databinding.FragmentMainListBinding
import ru.itis.androidhomework.domain.model.FeaturesModel
import ru.itis.androidhomework.presentation.MainActivity
import ru.itis.androidhomework.presentation.adapter.recycler.ListAdapter
import ru.itis.androidhomework.presentation.screens.DetailsInfo.FeatureDetailsFragment
import ru.itis.androidhomework.utils.hideKeyboardExtension
import ru.itis.androidhomework.utils.observe

@AndroidEntryPoint
class MainListFragment : Fragment(R.layout.fragment_main_list) {

    private var viewBinding: FragmentMainListBinding? = null

    private var rvAdapter: ListAdapter? = null

    private val viewModel: MainListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMainListBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeData()
    }

    private fun initRecycle(currentDataList: List<FeaturesModel>) {
        rvAdapter = ListAdapter(
            onItemClick = ::onItemClick,
        )
        rvAdapter?.setData(currentDataList)

        viewBinding?.mainListRecycle?.apply {
            adapter = rvAdapter
        }
    }

    private fun observeData() {
        with(viewModel) {
            loadingState.observe(viewLifecycleOwner) { isLoading ->
                viewBinding?.apply {
                    shimmerFrameLayout.isVisible = isLoading
                    mainListRecycle.isVisible = !isLoading
                    if (isLoading) {
                        shimmerFrameLayout.startShimmer()
                    } else {
                        shimmerFrameLayout.stopShimmer()
                    }
                }
            }


            emptyState.observe(viewLifecycleOwner) { isVisible ->
                viewBinding?.emptyTextView?.isVisible = isVisible
            }

            inputFocusState.observe(viewLifecycleOwner) { isClearFocus ->
                viewBinding?.searchEt?.let {
                    if (isClearFocus) {
                        it.requestFocus()
                    } else {
                        it.clearFocus()
                        hideKeyboardExtension(it)
                    }
                }
            }

            featureListState.observe(viewLifecycleOwner) { featureList ->
                initRecycle(featureList)
            }

            lifecycleScope.launch {
                errorsChannel.consumeEach { error ->
                    val errorMessage = error.message ?: getString(R.string.error_unknown)
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(getString(R.string.error_title))
                        .setMessage(errorMessage)
                        .setPositiveButton(android.R.string.ok, null)
                        .show()
                }
            }
        }
    }

    private fun initView() {
        viewBinding?.let { binding ->
            with(binding) {
                searchEt.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH && !viewModel.isActionInProgress.value) {
                        viewModel.getCoordinates(query = searchEt.text.toString())
                        true
                    } else {
                        false
                    }
                }

            }
        }
    }

    fun onItemClick(position: Int) {
        val item = rvAdapter?.getItemAt(position)
        item?.let { viewModel.goToFeatureDetails(it.xid) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    companion object {
        const val TAG = "MainListFragment"
    }
}