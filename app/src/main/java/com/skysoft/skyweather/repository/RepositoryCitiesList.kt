package com.skysoft.skyweather.repository

import com.skysoft.skyweather.model.City

interface RepositoryCitiesList {
    fun getRussianCities(): List<City>
    fun getWorldCities(): List<City>
}