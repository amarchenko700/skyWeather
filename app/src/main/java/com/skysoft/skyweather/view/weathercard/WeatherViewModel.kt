package com.skysoft.skyweather.view.weathercard

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skysoft.skyweather.model.*
import com.skysoft.skyweather.repository.RepositoryLocalImpl
import com.skysoft.skyweather.repository.RepositoryRemoteImpl
import com.skysoft.skyweather.utils.App
import com.skysoft.skyweather.view.AppStateWeather
import java.util.*

class WeatherViewModel(
    private val liveData: MutableLiveData<AppStateWeather> = MutableLiveData()
) : ViewModel() {

    private lateinit var cityToLoad: City
    private val repositoryRemoteImpl: RepositoryRemoteImpl = RepositoryRemoteImpl()
    private val repositoryLocalImpl: RepositoryLocalImpl = RepositoryLocalImpl()

    fun getLiveData(): LiveData<AppStateWeather> {
        return liveData
    }

    fun getWeatherFromRepository(city: City) {
        cityToLoad = city
        Thread{
            val weather = repositoryLocalImpl.getWeatherForCityName(city.name)
            App.getAppInstance().sendBroadcast(Intent(ACTION_GETTING_WEATHER_FROM_LOCAL_DB).apply {
                this.putExtra(WEATHER_KEY, weather)
            })
        }.start()
    }

    fun saveLoadedWeather(weather: Weather) {
        repositoryLocalImpl.saveWeather(cityToLoad, weather)
    }

    fun onReceive(intent: Intent?) {
        intent?.let {
            if (it.action == "android.intent.action.AIRPLANE_MODE") {
                it.getBooleanExtra("state", false).let { stateAM ->
                    if (!stateAM) {
                        liveData.value = AppStateWeather.AvailabilityOfTheInternet(true)
                        Timer().schedule(RemindTaskGetWeather(), 7000)
                    }
                }
            } else if (it.action == ACTION_ON_LOAD_WEATHER) {
                it.getParcelableExtra<WeatherDTO>(WEATHER_KEY)?.let { weatherDTO ->
                    repositoryRemoteImpl.getConvertedWeatherFromWeatherDTO(weatherDTO, cityToLoad)
                        .let { weather ->
                            saveLoadedWeather(weather)
                            liveData.value = AppStateWeather.SuccessLoadWeather(weather)
                        }
                }

            } else if (it.action == ACTION_ON_ERROR_LOAD_WEATHER ||
                it.action == ACTION_ON_ERROR_NO_INTERNET
            ) {
                it.getStringExtra(ERROR_KEY)?.let { errorString ->
                    liveData.value = AppStateWeather.Error(errorString)
                }

            }else if(it.action == ACTION_GETTING_WEATHER_FROM_LOCAL_DB){
                if (it.getParcelableExtra<Weather>(WEATHER_KEY) == null) {
                    repositoryRemoteImpl.getWeather(cityToLoad)
                } else {
                    liveData.value = AppStateWeather.SuccessLoadWeather(it.getParcelableExtra<Weather>(WEATHER_KEY)!!)
                }
            } else {
            }
        }
    }

    inner class RemindTaskGetWeather() : TimerTask() {
        override fun run() {
            getWeatherFromRepository(cityToLoad)
        }
    }
}
