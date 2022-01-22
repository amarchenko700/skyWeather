package com.skysoft.skyweather.model

interface CitiesRepository {
    fun getRussianCities(): List<City>
    fun getWorldCities(): List<City>
}