package ru.itis.androidhomework.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.AndroidEntryPoint
import ru.itis.androidhomework.R
import ru.itis.androidhomework.databinding.ActivityMainBinding
import ru.itis.androidhomework.presentation.screens.MainList.MainListFragment

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val containerId: Int = R.id.fragmentContainer
    private var viewBinding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding!!.root)
        if(savedInstanceState == null) {
            initView()
        }
    }

    private fun initView() {
        supportFragmentManager.beginTransaction()
            .add(
                containerId,
                MainListFragment(),
                MainListFragment.TAG
            ).commit()
    }

    fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(containerId, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding = null
    }
}

