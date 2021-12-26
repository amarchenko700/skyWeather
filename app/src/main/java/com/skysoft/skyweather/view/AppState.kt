package com.skysoft.skyweather.view

import com.skysoft.skyweather.model.City
import com.skysoft.skyweather.model.Weather

sealed class AppState {
    data class LoadingCities(val progress: Int) : AppState()
    data class LoadingWeather(val city: City) : AppState()
    object SuccessLoadCities : AppState()
    data class SuccessLoadWeather(val weatherData: Weather) : AppState()
    data class Error(val error:Throwable, val city: City) : AppState()
    data class OpenCityCard(val city: City) : AppState()
}
