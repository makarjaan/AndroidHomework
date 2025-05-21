package ru.itis.androidhomework.navigation

import androidx.navigation.NavController

interface Nav {

    interface Provider {
        fun getNavController(): NavController?
    }

    fun setNavProvider(navProvider: Provider)

    fun clearNavProvider(navProvider: Provider)
}