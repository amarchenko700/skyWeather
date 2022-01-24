package com.skysoft.skyweather.utils

import android.app.Application
import com.google.gson.GsonBuilder
import com.skysoft.skyweather.model.WeatherApi
import com.skysoft.skyweather.model.YANDEX_API_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContext: Application() {

    private val retrofit = Retrofit.Builder()
        .baseUrl(YANDEX_API_URL)
        .addConverterFactory(
            GsonConverterFactory.create(
            GsonBuilder().setLenient().create()
        ))
        .build().create(WeatherApi::class.java)

    fun getRetrofit(): WeatherApi? {
        return retrofit
    }
}