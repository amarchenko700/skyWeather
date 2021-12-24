package com.skysoft.skyweather.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.skysoft.skyweather.R
import com.skysoft.skyweather.databinding.FragmentCityBinding
import com.skysoft.skyweather.model.City

class CityFragment(city: City): Fragment() {

    private var city: City = city
    private var _binding: FragmentCityBinding? = null
    private val binding: FragmentCityBinding
        get() {
            return _binding!!
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCityBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillCardCity(city)
    }

    fun fillCardCity(city: City){
        requireActivity().findViewById<TextView>(R.id.city_name_textview).setText(city.name)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}