package com.skysoft.skyweather.repository

import com.skysoft.skyweather.model.City
import com.skysoft.skyweather.model.Weather

interface RepositoryHistoryWeather {
    fun getAllHistoryWeather(): List<Weather>
    fun saveWeather(city: City, weather: Weather)
    fun getWeatherForCityName(cityName: String): Weather?
}