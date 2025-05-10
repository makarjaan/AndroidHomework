package ru.itis.androidhomework.navigation

interface NavMain {

    fun initNavMain(parent: Nav)

    fun goToSearchPage()

    fun goToDetailPage(xid: String)

    fun goToRegistrationPage()

    fun goToLoginPage()
}