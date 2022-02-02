package com.skysoft.skyweather.repository

import com.skysoft.skyweather.model.City
import com.skysoft.skyweather.model.Weather

interface RepositoryHistoryWeather {
    fun saveWeather(city: City, weather: Weather)
    fun getWeatherForCityName(cityName: String, currentDateString: String): Weather?
}