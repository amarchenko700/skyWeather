package com.skysoft.skyweather.view.weathercard

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.skysoft.skyweather.BuildConfig
import com.skysoft.skyweather.view.AppState
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection
import androidx.core.content.ContextCompat.getSystemService

import android.net.ConnectivityManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startForegroundService
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.skysoft.skyweather.model.*
import com.skysoft.skyweather.utils.InternetService
import com.skysoft.skyweather.utils.MyWorker

class WeatherViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData()
    ) : ViewModel() {

    fun getLiveData(): LiveData<AppState> {
        return liveData
    }

    private fun getWeatherFromServer(city: City, context: Context) {
        context.startService(Intent(context, InternetService::class.java).apply {
            putExtra(CITY_KEY, city)
        })

//        liveData.value = AppState.Loading(0)
//        val handler = Handler(Looper.getMainLooper()!!)
//        Thread {
//            val url =
//                URL("https://api.weather.yandex.ru/v2/informers?lat=${city.latitude}&lon=${city.longitude}")
//            val urlConnection = (url.openConnection() as HttpsURLConnection).apply {
//                requestMethod = "GET"
//                readTimeout = 10000
//                addRequestProperty("X-Yandex-API-Key", BuildConfig.WEATHER_API_KEY)
//            }
//            try {
//                val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
//                val weather = Gson().fromJson(getLines(reader), WeatherDTO::class.java)
//                handler.post {
//                    liveData.postValue(AppState.SuccessLoadWeather(weather))
//                }
//            } catch (e: Exception) {
//                handler.post {
//                    liveData.postValue(AppState.Error(e))
//                }
//            } finally {
//                urlConnection.disconnect()
//            }
//
//        }.start()
    }

    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }

    fun getWeather(city: City, context: Context) {
        getWeatherFromServer(city, context)
    }

    fun onReceive(context: Context?, intent: Intent?){
        intent?.let {
            if (it.action == "android.intent.action.AIRPLANE_MODE") {
                it.getBooleanExtra("state", false).let { stateAM ->
                    if (!stateAM) {
                        Toast.makeText(context, "Появился интернет!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else if (it.action == ACTION_ON_LOAD_WEATHER) {
                it.getParcelableExtra<WeatherDTO>(WEATHER_KEY)?.let { weatherDTO->
                    liveData.value = AppState.SuccessLoadWeather(weatherDTO)
                }

            } else if (it.action == ACTION_ON_ERROR_LOAD_WEATHER) {
                it.getStringExtra(ERROR_KEY)?.let { errorString->
                    liveData.value = AppState.Error(errorString)
                }
            }else{}
        }
    }

}