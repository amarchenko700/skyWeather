package com.skysoft.skyweather.view.weathercard

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.skysoft.skyweather.BuildConfig
import com.skysoft.skyweather.model.City
import com.skysoft.skyweather.model.WeatherDTO
import com.skysoft.skyweather.view.AppState
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection
import androidx.core.content.ContextCompat.getSystemService

import android.net.ConnectivityManager
import androidx.core.content.ContextCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.skysoft.skyweather.utils.MyWorker


class WeatherViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData()
    ) : ViewModel() {

    fun getLiveData(): LiveData<AppState> {
        return liveData
    }

    private fun getWeatherFromServer(lat: Double, lon: Double) {
//        val manager = WorkManager.getInstance(context)
//        val worker = OneTimeWorkRequest.Builder(MyWorker::class.java)
//            //   .setInitialDelay(5, TimeUnit.SECONDS)
//            .build()
//        manager.enqueue(worker)


        liveData.value = AppState.Loading(0)
        val handler = Handler(Looper.getMainLooper()!!)
        Thread {
            val url =
                URL("https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon")
            val urlConnection = (url.openConnection() as HttpsURLConnection).apply {
                requestMethod = "GET"
                readTimeout = 10000
                addRequestProperty("X-Yandex-API-Key", BuildConfig.WEATHER_API_KEY)
            }
            try {
                val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val weather = Gson().fromJson(getLines(reader), WeatherDTO::class.java)
                handler.post {
                    liveData.postValue(AppState.SuccessLoadWeather(weather))
                }
            } catch (e: Exception) {
                handler.post {
                    liveData.postValue(AppState.Error(e))
                }
            } finally {
                urlConnection.disconnect()
            }

        }.start()
    }

    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }

    fun getWeather(lat: Double, lon: Double) {
        getWeatherFromServer(lat, lon)
    }

}