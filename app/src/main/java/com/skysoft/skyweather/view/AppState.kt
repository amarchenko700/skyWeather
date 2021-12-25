package com.skysoft.skyweather.view

import com.skysoft.skyweather.model.City
import com.skysoft.skyweather.model.Weather

sealed class AppState {
    data class Loading(val progress: Int) : AppState()
    class SuccessLoadCities() : AppState()
    data class SuccessLoadWeather(val weatherData: Weather) : AppState()
    data class Error(val error:Throwable) : AppState()
    data class openCityCard(val city: City) : AppState()
}