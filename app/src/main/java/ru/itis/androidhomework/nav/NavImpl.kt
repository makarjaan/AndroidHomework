package ru.itis.androidhomework.nav

import ru.itis.androidhomework.navigation.Nav
import ru.itis.androidhomework.navigation.NavMain
import javax.inject.Inject

class NavImpl @Inject constructor(
    private val navigatorDelegate: NavigatorDelegate,
    private val navMain: NavMain
) : Nav, NavMain by navMain {

    override fun setNavProvider(navProvider: Nav.Provider) {
        navigatorDelegate.setNavProvider(navProvider = navProvider)
    }

    override fun clearNavProvider(navProvider: Nav.Provider) {
        navigatorDelegate.clearNavProvider(navProvider = navProvider)
    }


}