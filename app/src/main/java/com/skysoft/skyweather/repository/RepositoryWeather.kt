package com.skysoft.skyweather.repository

import android.content.Context
import com.skysoft.skyweather.model.City
import com.skysoft.skyweather.model.WeatherDTO
import retrofit2.Callback

interface RepositoryWeather {
    fun getWeatherFromServer(city: City, callback: Callback<WeatherDTO>)
    fun getWeather(city: City, context: Context)
}