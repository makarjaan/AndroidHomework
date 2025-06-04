package ru.itis.androidhomework.nav

import android.os.Bundle
import ru.itis.androidhomework.R
import ru.itis.androidhomework.navigation.Nav
import ru.itis.androidhomework.navigation.NavMain
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

    override fun goToDetailPage(xid: String) {
        val bundle = Bundle().apply {
            putString("xid", xid)
        }

        navigatorDelegate.navigate(R.id.action_global_feature_details_page, bundle)
    }

    override fun goToRegistrationPage() {
        navigatorDelegate.navigate(
            action = R.id.destination_feature_registration_page
        )
    }

    override fun goToLoginPage() {
        navigatorDelegate.navigate(
            action = R.id.destination_feature_login_page
        )
    }

    override fun goToChartPage() {
        navigatorDelegate.navigate(
            action = R.id.destination_feature_chart_page
        )
    }


}