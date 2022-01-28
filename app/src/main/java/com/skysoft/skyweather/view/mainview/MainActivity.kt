package com.skysoft.skyweather.view.mainview

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.skysoft.skyweather.R
import com.skysoft.skyweather.databinding.ActivityMainBinding
import com.skysoft.skyweather.view.citieslist.ListCitiesFragment

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() {
            return _binding!!
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            openListCities()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun openListCities() {
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragment_container_framelayout,
                ListCitiesFragment.newInstance()
            )
            .commit()
    }
}