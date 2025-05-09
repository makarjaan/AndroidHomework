package ru.itis.androidhomework.presentation.navigation.nav

interface NavMain {

    fun initNavMain(parent: Nav)

    fun goToSearchPage()

    fun goToDetailsPage(xid: String)
}