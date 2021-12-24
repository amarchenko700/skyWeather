package com.skysoft.skyweather.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.skysoft.skyweather.databinding.FragmentCityBinding
import com.skysoft.skyweather.model.City

class CityFragment: Fragment() {

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

    fun fillCardCity(city: City){

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}