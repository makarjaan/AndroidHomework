package ru.itis.androidhomework.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.itis.androidhomework.R
import ru.itis.androidhomework.databinding.ActivityMainBinding
import ru.itis.androidhomework.presentation.navigation.nav.Nav
import ru.itis.androidhomework.presentation.screens.MainList.MainListFragment
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Nav.Provider {

    @Inject
    lateinit var nav: Nav

    private val containerId: Int = R.id.fragmentContainer
    private var viewBinding: ActivityMainBinding? = null
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding!!.root)
        setupNavigation()
    }

    private fun setupNavigation() {
        if (navController == null) {
            val navHost = supportFragmentManager.findFragmentById(containerId) as NavHostFragment
            navController = navHost.navController
        }
        nav.setNavProvider(navProvider = this)
    }

    override fun getNavController(): NavController? {
        return navController
    }


    override fun onDestroy() {
        super.onDestroy()
        viewBinding = null
        if (this::nav.isInitialized) {
            nav.clearNavProvider(navProvider = this)
        }
    }
}

