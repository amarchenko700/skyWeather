package com.skysoft.skyweather.view

import com.skysoft.skyweather.model.City
import com.skysoft.skyweather.model.Weather

sealed class AppState {
    data class Loading(val progress:Int) : AppState()
    data class Success(val weatherData:List<Weather>): AppState()
    data class SuccessLoadWeather(val weatherData: Weather) : AppState()
    data class Error(val error:Throwable, val city: City) : AppState()
}
