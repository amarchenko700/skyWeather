package com.skysoft.skyweather.view.weathercard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skysoft.skyweather.model.City
import com.skysoft.skyweather.model.WeatherRepositoryImpl
import com.skysoft.skyweather.view.AppState

class WeatherViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: WeatherRepositoryImpl = WeatherRepositoryImpl()
) : ViewModel() {

    fun getLiveData(): LiveData<AppState> {
        return liveData
    }

    fun getWeatherFromServer(city: City) {
        liveData.value = AppState.Loading(0)
        Thread {
            Thread.sleep(1500)
            with(liveData) {
                if (city.requestsCount >= 2) {
                    postValue(
                        AppState.SuccessLoadWeather(
                            repositoryImpl.getWeatherFromServer(
                                city
                            )
                        )
                    )
                } else {
                    postValue(AppState.Error(IllegalStateException(""), city))
                }
            }
            city.requestsCount++
        }.start()
    }

    fun getWeather(city: City) {
        getWeatherFromServer(city)
    }
}