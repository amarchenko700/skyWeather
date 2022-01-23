package com.skysoft.skyweather.utils

import android.app.IntentService
import android.content.Intent
import com.skysoft.skyweather.BuildConfig
import com.skysoft.skyweather.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.util.stream.Collectors

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
            getWeatherFromServer(it)
        }
    }

    private fun getWeatherFromServerRetrofit(city: City, callback: Callback<WeatherDTO>) {
        val retrofit = AppRepo().getRetrofit()
        retrofit?.let {
            it.getWeather(BuildConfig.WEATHER_API_KEY, city.latitude, city.longitude)
                .enqueue(callback)
        }
    }

    private fun getWeatherFromServer(city: City) {
        getWeatherFromServerRetrofit(city, callback)
//        val url =
//            URL("https://api.weather.yandex.ru/v2/informers?lat=${city.latitude}&lon=${city.longitude}")
//        val urlConnection = (url.openConnection() as HttpsURLConnection).apply {
//            requestMethod = "GET"
//            readTimeout = 10000
//            addRequestProperty(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY)
//        }
//
//        try {
//            val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
//            val weatherDTO = Gson().fromJson(getLines(reader), WeatherDTO::class.java)
////            LocalBroadcastManager.getInstance(applicationContext)
////                .sendBroadcast(Intent(ACTION_ON_LOAD_WEATHER).apply {
////                    this.putExtra(WEATHER_KEY, weatherDTO)
////                })
//            sendBroadcast(Intent(ACTION_ON_LOAD_WEATHER).apply {
//                this.putExtra(WEATHER_KEY, weatherDTO)
//            })
//
//        } catch (e: Exception) {
////            LocalBroadcastManager.getInstance(applicationContext)
////                .sendBroadcast(Intent(ACTION_ON_ERROR_LOAD_WEATHER).apply {
////                    this.putExtra(ERROR_KEY, e.toString())
////                })
//            sendBroadcast(Intent(ACTION_ON_ERROR_LOAD_WEATHER).apply {
//                this.putExtra(ERROR_KEY, e.toString())
//            })
//
//        } finally {
//            urlConnection.disconnect()
//        }
    }

    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }
}