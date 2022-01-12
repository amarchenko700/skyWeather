package com.skysoft.skyweather.model

class WeatherRepositoryImpl : WeatherRepository {

    override fun getWeatherFromServer(city: City) = Weather(city)

    override fun getWeatherFromLocalStorageRus() = getRussianCities()

    override fun getWeatherFromLocalStorageWorld() = getWorldCities()
}