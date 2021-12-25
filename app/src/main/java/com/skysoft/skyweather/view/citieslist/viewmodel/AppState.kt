package com.skysoft.skyweather.view.citieslist.viewmodel

import com.skysoft.skyweather.model.City
import com.skysoft.skyweather.model.Weather

sealed class AppState {
    data class Loading(val progress: Int) : AppState()
    data class Success(val weatherData: Weather) : AppState()
    data class Error(val error:Throwable) : AppState()
    data class openCityCard(val city: City) : AppState()
}