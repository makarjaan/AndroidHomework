package ru.itis.androidhomework.presentation

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import ru.itis.androidhomework.R
import ru.itis.androidhomework.databinding.ActivityMainBinding
import ru.itis.androidhomework.presentation.screens.MainList.MainListFragment

class MainActivity : FragmentActivity() {

    private val containerId: Int = R.id.fragmentContainer
    private var viewBinding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
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

    override fun onDestroy() {
        super.onDestroy()
        viewBinding = null
    }
}

