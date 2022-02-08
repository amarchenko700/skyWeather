package com.skysoft.skyweather.view.mainview

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.skysoft.skyweather.R
import com.skysoft.skyweather.databinding.ActivityMainBinding
import com.skysoft.skyweather.model.openFragment
import com.skysoft.skyweather.myMap.MapsFragment
import com.skysoft.skyweather.view.citieslist.ListCitiesFragment
import com.skysoft.skyweather.view.contacts.ContactsFragment

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            openFragment(this, ListCitiesFragment.newInstance(), false)
        }
        setSupportActionBar(binding.toolbar)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_navigation_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.contacts) {
            openFragment(this, ContactsFragment.newInstance(), true)
            return true
        } else if (item.itemId == R.id.menu_google_maps) {
            openFragment(this, MapsFragment(), true)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}