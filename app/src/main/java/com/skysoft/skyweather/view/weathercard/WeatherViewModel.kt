package com.skysoft.skyweather.view.weathercard

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.skysoft.skyweather.model.City
import com.skysoft.skyweather.model.Weather
import com.skysoft.skyweather.view.AppState
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class WeatherViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData()
    ) : ViewModel() {

    fun getLiveData(): LiveData<AppState> {
        return liveData
    }

    fun getWeatherFromServer(city: City) {
        liveData.value = AppState.Loading(0)
        val handler = Handler(Looper.getMainLooper()!!)
        Thread {
            val url =
                URL("https://api.weather.yandex.ru/v2/informers?lat=${city.latitude}&lon=${city.longitude}")
            val urlConnection = (url.openConnection() as HttpsURLConnection).apply {
                requestMethod = "GET"
                readTimeout = 10000
                addRequestProperty("X-Yandex-API-Key", "76597d0d-a647-418e-9e6e-d0f0a7be6d9c")
            }
            try {
                val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val weather = Gson().fromJson(getLines(reader), Weather::class.java)
                handler.post {
                    liveData.postValue(AppState.SuccessLoadWeather(weather))
                }
            } catch (e: Exception) {
                handler.post {
                    liveData.postValue(AppState.Error(e, city))
                }
            } finally {
                urlConnection.disconnect()
            }

        }.start()
    }

    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }

    fun getWeather(city: City) {
        getWeatherFromServer(city)
    }
}