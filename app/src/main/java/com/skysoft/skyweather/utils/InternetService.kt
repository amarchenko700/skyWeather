package com.skysoft.skyweather.utils

import android.app.IntentService
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.skysoft.skyweather.BuildConfig
import com.skysoft.skyweather.model.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class InternetService(name: String = "") : IntentService(name) {

    override fun onHandleIntent(intent: Intent?) {
        intent?.getParcelableExtra<City>(CITY_KEY)?.let {
            getWeatherFromServer(it)
        }
    }

    private fun getWeatherFromServer(city: City) {

        val url =
            URL("https://api.weather.yandex.ru/v2/informers?lat=${city.latitude}&lon=${city.longitude}")
        val urlConnection = (url.openConnection() as HttpsURLConnection).apply {
            requestMethod = "GET"
            readTimeout = 10000
            addRequestProperty(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY)
        }

        try {
            val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
            val weatherDTO = Gson().fromJson(getLines(reader), WeatherDTO::class.java)
//            LocalBroadcastManager.getInstance(applicationContext)
//                .sendBroadcast(Intent(ACTION_ON_LOAD_WEATHER).apply {
//                    this.putExtra(WEATHER_KEY, weatherDTO)
//                })
            sendBroadcast(Intent(ACTION_ON_LOAD_WEATHER).apply {
                this.putExtra(WEATHER_KEY, weatherDTO)
            })

        } catch (e: Exception) {
//            LocalBroadcastManager.getInstance(applicationContext)
//                .sendBroadcast(Intent(ACTION_ON_ERROR_LOAD_WEATHER).apply {
//                    this.putExtra(ERROR_KEY, e.toString())
//                })
            sendBroadcast(Intent(ACTION_ON_ERROR_LOAD_WEATHER).apply {
                this.putExtra(ERROR_KEY, e.toString())
            })

        } finally {
            urlConnection.disconnect()
        }
    }

    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }
}