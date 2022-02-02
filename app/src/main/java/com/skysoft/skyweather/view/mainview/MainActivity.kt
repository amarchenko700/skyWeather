package com.skysoft.skyweather.view.mainview

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.skysoft.skyweather.R
import com.skysoft.skyweather.databinding.ActivityMainBinding
import com.skysoft.skyweather.view.citieslist.ListCitiesFragment
import com.skysoft.skyweather.view.contacts.ContactsFragment

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
        setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_navigation_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.contacts) {
            openFragmentContacts()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openFragmentContacts() {
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragment_container_framelayout,
                ContactsFragment.newInstance()
            )
            .addToBackStack(null)
            .commit()
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