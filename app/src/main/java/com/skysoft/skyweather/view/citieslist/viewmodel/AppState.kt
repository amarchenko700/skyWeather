package com.skysoft.skyweather.view.citieslist.viewmodel

import com.skysoft.skyweather.model.City

sealed class AppState {
    data class Loading(var progress: Int) : AppState()
    data class Success(var weatherData: String) : AppState()
    data class Error(var error:Throwable) : AppState()
    data class CityCard(var city: City) : AppState()
}