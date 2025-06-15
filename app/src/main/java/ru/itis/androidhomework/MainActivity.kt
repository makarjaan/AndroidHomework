package ru.itis.androidhomework

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ru.itis.androidhomework.databinding.ActivityMainBinding

class MainActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pieCustom.setDataChart(
            listOf(1 to 25, 2 to 42, 3 to 33)
        )
        Log.d("TEST-TAG", "йоу")
        binding.pieCustom.startAnimation()
    }
}