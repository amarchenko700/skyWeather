package com.skysoft.skyweather.view

import com.skysoft.skyweather.model.City
import com.skysoft.skyweather.model.WeatherDTO

sealed class AppStateWeather {
    data class Loading(val progress:Int) : AppStateWeather()
    data class SuccessLoadWeather(val weatherDTO: WeatherDTO) : AppStateWeather()
    data class Error(val error:String) : AppStateWeather()
    data class ErrorNoInternet(val error:String) : AppStateWeather()
    data class AvailabilityOfTheInternet(val availability:Boolean) : AppStateWeather()

}

sealed class AppStateListCities{
    data class LoadedCitiesList(val citiesList: List<City>): AppStateListCities()
}