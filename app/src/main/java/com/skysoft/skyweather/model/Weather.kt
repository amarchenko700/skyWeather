package com.skysoft.skyweather.model

data class Weather(
    val city: City = getDefaultCity(),
    val temperature: Int = 20,
    val feelsLike: Int = 20
)

data class City(val name: String, val latitude: Double, val longitude: Double)

fun getDefaultCity() = City("Москва", 55.7382489349867, 37.63742208612825)