package com.skysoft.skyweather.model

class WeatherRepositoryImpl: WeatherRepository {

    override fun getWeatherFromServer(city: City): Weather {
        return Weather(city)
    }

    override fun getWeatherFromLocalStorageRus(): List<Weather> {
        return getRussianCities()
    }

    override fun getWeatherFromLocalStorageWorld(): List<Weather> {
        return getWorldCities()
    }


}