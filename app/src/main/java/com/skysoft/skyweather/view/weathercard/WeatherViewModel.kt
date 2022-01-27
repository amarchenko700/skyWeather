package com.skysoft.skyweather.view.weathercard

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skysoft.skyweather.model.*
import com.skysoft.skyweather.repository.RepositoryImpl
import com.skysoft.skyweather.view.AppStateWeather
import java.util.*

class WeatherViewModel(
    private val liveData: MutableLiveData<AppStateWeather> = MutableLiveData()
) : ViewModel() {

    private lateinit var cityToLoad: City
    private val repositoryImpl: RepositoryImpl = RepositoryImpl()

    fun getLiveData(): LiveData<AppStateWeather> {
        return liveData
    }

    fun getWeatherFromRepository(city: City, context: Context) {
        cityToLoad = city
        repositoryImpl.getWeather(city, context)
    }

    fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (it.action == "android.intent.action.AIRPLANE_MODE") {
                it.getBooleanExtra("state", false).let { stateAM ->
                    if (!stateAM) {
                        liveData.value = AppStateWeather.AvailabilityOfTheInternet(true)
                        context?.let { ctx -> Timer().schedule(RemindTaskGetWeather(ctx), 7000) }
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

    inner class RemindTaskGetWeather(val context: Context) : TimerTask() {
        override fun run() {
            getWeatherFromRepository(cityToLoad, context)
        }
    }
}
