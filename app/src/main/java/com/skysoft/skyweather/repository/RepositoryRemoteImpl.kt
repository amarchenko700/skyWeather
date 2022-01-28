package com.skysoft.skyweather.repository

import android.content.Intent
import com.skysoft.skyweather.BuildConfig
import com.skysoft.skyweather.model.CITY_KEY
import com.skysoft.skyweather.model.City
import com.skysoft.skyweather.model.Weather
import com.skysoft.skyweather.model.WeatherDTO
import com.skysoft.skyweather.utils.App
import com.skysoft.skyweather.utils.InternetService
import retrofit2.Callback

class RepositoryRemoteImpl : RepositoryWeather {

    override fun getWeatherFromServer(city: City, callback: Callback<WeatherDTO>) {
        App().getRetrofit()?.let {
            it.getWeatherFromRemoteServer(
                BuildConfig.WEATHER_API_KEY,
                city.latitude,
                city.longitude
            )
                .enqueue(callback)
        }
    }

    override fun getWeather(city: City) {

        App.getAppInstance().applicationContext.let {
            it.startService(Intent(it, InternetService::class.java).apply {
                putExtra(CITY_KEY, city)
            })
        }
    }

    override fun getConvertedWeatherFromWeatherDTO(weatherDTO: WeatherDTO, city: City): Weather {
        return Weather(
            city.name,
            weatherDTO.fact.temp,
            weatherDTO.fact.feelsLike,
            weatherDTO.fact.icon
        )
    }
}