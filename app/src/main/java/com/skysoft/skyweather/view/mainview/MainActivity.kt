package com.skysoft.skyweather.view.mainview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.skysoft.skyweather.R
import com.skysoft.skyweather.databinding.ActivityMainBinding
import com.skysoft.skyweather.view.citieslist.ListCitiesFragment

private const val FRAGMENT_LIST_CITIES_TAG = "FRAGMENT_LIST_CITIES_TAG"

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
        openListCities()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun openListCities(){
        val listCitiesFragment =
            supportFragmentManager.findFragmentByTag(FRAGMENT_LIST_CITIES_TAG) ?: ListCitiesFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container_framelayout, listCitiesFragment, FRAGMENT_LIST_CITIES_TAG)
            .commit()
    }
}