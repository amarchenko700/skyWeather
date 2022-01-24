package com.skysoft.skyweather.view.weathercard

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skysoft.skyweather.model.*
import com.skysoft.skyweather.utils.InternetService
import com.skysoft.skyweather.view.AppStateWeather
import java.util.*

class WeatherViewModel(
    private val liveData: MutableLiveData<AppStateWeather> = MutableLiveData()
) : ViewModel() {

    private lateinit var cityToLoad: City

    fun getLiveData(): LiveData<AppStateWeather> {
        return liveData
    }

    private fun getWeatherFromServer(city: City, context: Context?) {
        context?.let {
            cityToLoad = city
            context.startService(Intent(context, InternetService::class.java).apply {
                putExtra(CITY_KEY, city)
            })
        }
    }

    fun getWeather(city: City, context: Context) {
        getWeatherFromServer(city, context)
    }

    fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (it.action == "android.intent.action.AIRPLANE_MODE") {
                it.getBooleanExtra("state", false).let { stateAM ->
                    if (!stateAM) {
                        liveData.value = AppStateWeather.AvailabilityOfTheInternet(true)
                        Timer().schedule(RemindTaskRequestToServer(context), 7000)
                    }
                }
            } else if (it.action == ACTION_ON_LOAD_WEATHER) {
                it.getParcelableExtra<WeatherDTO>(WEATHER_KEY)?.let { weatherDTO ->
                    liveData.value = AppStateWeather.SuccessLoadWeather(weatherDTO)
                }

            } else if (it.action == ACTION_ON_ERROR_LOAD_WEATHER ||
                it.action == ACTION_ON_ERROR_NO_INTERNET
            ) {
                it.getStringExtra(ERROR_KEY)?.let { errorString ->
                    liveData.value = AppStateWeather.Error(errorString)
                }

            } else {
            }
        }
    }

    inner class RemindTaskRequestToServer(val context: Context?) : TimerTask() {
        override fun run() {
            getWeatherFromServer(cityToLoad, context)
        }
    }
}
