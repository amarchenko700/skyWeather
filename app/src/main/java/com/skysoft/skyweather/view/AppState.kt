package com.skysoft.skyweather.view

import com.skysoft.skyweather.model.City
import com.skysoft.skyweather.model.WeatherDTO

sealed class AppState {
    data class Loading(val progress:Int) : AppState()
    data class LoadedCitiesList(val citiesList: List<City>): AppState()
    data class SuccessLoadWeather(val weatherDTO: WeatherDTO) : AppState()
    data class Error(val error:Throwable, val city: City) : AppState()
}
