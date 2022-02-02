package com.skysoft.skyweather.repository

import com.skysoft.skyweather.model.City
import com.skysoft.skyweather.model.Weather
import com.skysoft.skyweather.room.HistoryWeatherEntity
import com.skysoft.skyweather.utils.App

class RepositoryLocalImpl : RepositoryCitiesList, RepositoryHistoryWeather {

    override fun getRussianCities(): List<City> {
        return listOf(
            City("Москва", 55.755826, 37.617299900000035),
            City("Санкт-Петербург", 59.9342802, 30.335098600000038),
            City("Новосибирск", 55.00835259999999, 82.93573270000002),
            City("Екатеринбург", 56.83892609999999, 60.60570250000001),
            City("Нижний Новгород", 56.2965039, 43.936059),
            City("Казань", 55.8304307, 49.06608060000008),
            City("Челябинск", 55.1644419, 61.4368432),
            City("Омск", 54.9884804, 73.32423610000001),
            City("Ростов-на-Дону", 47.2357137, 39.701505),
            City("Уфа", 54.7387621, 55.972055400000045)
        )
    }

    override fun getWorldCities(): List<City> {
        return listOf(
            City("Лондон", 51.5085300, -0.1257400),
            City("Токио", 35.6895000, 139.6917100),
            City("Париж", 48.8534100, 2.3488000),
            City("Берлин", 52.52000659999999, 13.404953999999975),
            City("Рим", 41.9027835, 12.496365500000024),
            City("Минск", 53.90453979999999, 27.561524400000053),
            City("Стамбул", 41.0082376, 28.97835889999999),
            City("Вашингтон", 38.9071923, -77.03687070000001),
            City("Киев", 50.4501, 30.523400000000038),
            City("Пекин", 39.90419989999999, 116.40739630000007)
        )
    }

    override fun saveWeather(city: City, weather: Weather) {
        App.getHistoryWeatherDao().insert(
            convertWeatherToHistoryWeatherEntity(city, weather)
        )
    }

    override fun getWeatherForCityName(cityName: String, currentDateString: String): Weather? {
        return convertHistoryWeatherEntityToWeather(
            App.getHistoryWeatherDao().getHistoryWeather(cityName, currentDateString)
        )
    }

    private fun convertHistoryWeatherEntityToWeather(historyWeatherEntity: HistoryWeatherEntity?): Weather? {
        val weather = historyWeatherEntity?.let {
            Weather(
                it.cityName,
                it.temperature,
                it.feelsLike,
                it.icon,
                it.dataDate
            )
        }
        return weather
    }

    private fun convertWeatherToHistoryWeatherEntity(city: City, weather: Weather) =
        HistoryWeatherEntity(
            0,
            city.name,
            weather.temperature,
            weather.feelsLike,
            weather.icon
        )
}