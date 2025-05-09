package ru.itis.androidhomework.presentation.navigation.navImpl

import androidx.core.os.bundleOf
import ru.itis.androidhomework.R
import ru.itis.androidhomework.presentation.navigation.nav.Nav
import ru.itis.androidhomework.presentation.navigation.nav.NavMain
import ru.itis.androidhomework.presentation.navigation.NavigatorDelegate
import javax.inject.Inject

class NavMainImpl @Inject constructor(
    private val navigatorDelegate: NavigatorDelegate
): NavMain {

    private var parent: Nav? = null

    override fun initNavMain(parent: Nav) {
        this.parent = parent
    }

    override fun goToSearchPage() {
        navigatorDelegate.navigate(
            action = R.id.action_global_main_page
        )
    }

    override fun goToDetailsPage(xid: String) {
        val args = bundleOf(PARAM_KEY to xid)
        navigatorDelegate.navigate(
            action = R.id.action_global_feature_details_page,
            args = args
        )
    }

    companion object {
        const val PARAM_KEY = "xid"
    }
}