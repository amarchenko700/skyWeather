package com.skysoft.skyweather.view

import com.skysoft.skyweather.model.City
import com.skysoft.skyweather.model.Weather

sealed class AppStateWeather {
    data class Loading(val progress: Int) : AppStateWeather()
    data class SuccessLoadWeather(val weather: Weather) : AppStateWeather()
    data class Error(val error: String) : AppStateWeather()
    data class ErrorNoInternet(val error: String) : AppStateWeather()
    data class AvailabilityOfTheInternet(val availability: Boolean) : AppStateWeather()

}

sealed class AppStateListCities {
    data class LoadedCitiesList(val citiesList: List<City>) : AppStateListCities()
}

sealed class AppStateContacts {
    data class Error(val error: String) : AppStateContacts()
    data class SuccessLoadContacts(val s: String) : AppStateContacts()
}