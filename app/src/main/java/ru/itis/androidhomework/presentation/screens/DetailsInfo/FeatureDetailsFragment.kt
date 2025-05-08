package ru.itis.androidhomework.presentation.screens.DetailsInfo

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import ru.itis.androidhomework.R
import ru.itis.androidhomework.databinding.FragmentDetailsInfoBinding
import ru.itis.androidhomework.utils.lazyViewModel
import ru.itis.androidhomework.utils.observe
import javax.inject.Inject
import androidx.core.net.toUri
import com.google.android.material.dialog.MaterialAlertDialogBuilder

@AndroidEntryPoint
class FeatureDetailsFragment: Fragment() {

    private var viewBinding: FragmentDetailsInfoBinding? = null

    @Inject
    lateinit var vmFactory: FeatureDetailsViewModel.Factory

    private val viewModel: FeatureDetailsViewModel by lazyViewModel {
        vmFactory.create(
            xid = arguments?.getString(XID_KEY) ?: "",
            savedStateHandle = it
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentDetailsInfoBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getInfoById()
        observeData()
    }

    private fun observeData() {
        with(viewModel) {

            detailsState.observe(viewLifecycleOwner) { details ->
                with(viewBinding) {
                   this?.let {
                       placeImage.let { iv ->
                           Glide.with(requireContext())
                               .load(details.image)
                               .error(R.drawable.sample_image)
                               .into(placeImage)
                       }

                       placeName.text = details.name
                       val rateInt = details.rate.toIntOrNull() ?: 3
                       placeRating.text = getString(R.string.rating, rateInt)
                       placeAddress.text = details.address
                       placeDescription.text = details.text

                       if (details.wikipedia.isNotEmpty()) {
                           wikiLink.text = getString(R.string.hyperlink)
                           wikiLink.movementMethod = LinkMovementMethod.getInstance()

                           wikiLink.setOnClickListener {
                               val intent = Intent(Intent.ACTION_VIEW, details.wikipedia.toUri())
                               startActivity(intent)
                           }
                       }
                   }
                }
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

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    companion object {
        private const val XID_KEY = "xid"

        fun getInstance(
            xid: String
        ): FeatureDetailsFragment {
            return FeatureDetailsFragment().apply {
                arguments = bundleOf(XID_KEY to xid)
            }
        }
    }

}