package com.skysoft.skyweather.view.citieslist

import com.skysoft.skyweather.model.Weather

interface OnItemClickListener {
    fun onItemClick(weather: Weather)
}