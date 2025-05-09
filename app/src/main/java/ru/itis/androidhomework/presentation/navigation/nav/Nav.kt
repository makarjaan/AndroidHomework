package ru.itis.androidhomework.presentation.navigation.nav

import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController

interface Nav {

    interface Provider {
        fun getNavController(): NavController?
    }

    fun setNavProvider(navProvider: Provider)

    fun clearNavProvider(navProvider: Provider)
}