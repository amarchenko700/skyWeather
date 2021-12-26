package com.skysoft.skyweather.model

data class Weather(
    val city: City = getDefaultCity(),
    var temperature: Int = 20,
    var feelsLike: Int = 20
){
    init {
        val rand = (-20..20).random()
        this.temperature = rand
        this.feelsLike = rand - 2
    }
}

data class City(val name: String, val latitude: Double, val longitude: Double, var requestsCount: Int = 1)

fun getDefaultCity() = City("Москва", 55.7382489349867, 37.63742208612825)