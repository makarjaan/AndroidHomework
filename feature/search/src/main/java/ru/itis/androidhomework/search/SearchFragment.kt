package ru.itis.androidhomework.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import ru.itis.androidhomework.base.BaseFragment
import ru.itis.androidhomework.theme.AppHomeTheme

@AndroidEntryPoint
class SearchFragment : BaseFragment(R.layout.fragment_search) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            AppHomeTheme {
                SearchScreen(modifier = Modifier.padding(16.dp))
            }
        }
    }
}