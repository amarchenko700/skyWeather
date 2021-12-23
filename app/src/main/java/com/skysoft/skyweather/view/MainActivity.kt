package com.skysoft.skyweather.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.skysoft.skyweather.R
import com.skysoft.skyweather.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() {
            return _binding!!
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        openListCities()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun openListCities(){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container_framelayout, ListCitiesFragment.newInstance())
            .commit()
    }


}