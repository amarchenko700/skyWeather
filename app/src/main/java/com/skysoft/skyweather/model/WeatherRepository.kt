package com.skysoft.skyweather.model

interface WeatherRepository {
    fun getWeatherFromServer(city: City): Weather
    fun getWeatherFromLocalStorageRus(): List<Weather>
    fun getWeatherFromLocalStorageWorld(): List<Weather>
}