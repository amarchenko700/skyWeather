package com.skysoft.skyweather.model

interface WeatherRepository {
    fun getWeatherFromServer(): Weather
    fun getWeatherFromLocalStorage(): Weather
}