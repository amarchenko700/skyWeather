package com.skysoft.skyweather.model

interface WeatherRepository {
    fun getWeatherFromServer(city: City): Weather
    fun getWeatherFromLocalStorage(city: City): Weather
}