package com.skysoft.skyweather.model

object CitiesRepoImpl {
    private val cacheCities = mutableListOf<City>()

    init {
        cacheCities.add(
            City("Москва", 55.7382489349867, 37.63742208612825)
        )

        cacheCities.add(
            City("Санкт-Петербург", 59.922009107757695, 30.352046855740685)
        )

        cacheCities.add(
            City("Брянск", 53.261242304349615, 34.35621605878593)
        )

        cacheCities.add(
            City("Курск", 51.749413987903374, 36.206653839204066)
        )

        cacheCities.add(
            City("Белгород", 50.59484177034886, 36.58717280973799)
        )

        cacheCities.add(
            City("Воронеж", 51.66806593176428, 39.18009517943471)
        )
    }

    fun getCities(): List<City?>? {
        return ArrayList<City?>(cacheCities)
    }
}