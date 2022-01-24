package com.skysoft.skyweather.utils

import android.app.IntentService
import android.content.Intent
import com.skysoft.skyweather.BuildConfig
import com.skysoft.skyweather.model.*
import com.skysoft.skyweather.repository.RepositoryImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InternetService(name: String = "") : IntentService(name) {

    private val callback = object : Callback<WeatherDTO> {
        override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
            if (response.isSuccessful) {
                response.body()?.let {
                    sendBroadcast(Intent(ACTION_ON_LOAD_WEATHER).apply {
                        this.putExtra(WEATHER_KEY, it)
                    })
                }
            }
        }

        override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
            sendBroadcast(Intent(ACTION_ON_ERROR_LOAD_WEATHER).apply {
                this.putExtra(ERROR_KEY, t.toString())
            })
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        intent?.getParcelableExtra<City>(CITY_KEY)?.let {
            RepositoryImpl().getWeatherFromServer(it, callback)
        }
    }

}