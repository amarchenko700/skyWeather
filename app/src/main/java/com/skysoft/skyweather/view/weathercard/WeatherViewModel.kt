package com.skysoft.skyweather.view.weathercard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skysoft.skyweather.model.WeatherRepositoryImpl
import com.skysoft.skyweather.view.AppState

class WeatherViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: WeatherRepositoryImpl = WeatherRepositoryImpl()
) : ViewModel() {

    fun getLiveData(): LiveData<AppState> {
        return liveData
    }

    fun getWeatherFromServer() {
        liveData.value = AppState.Loading(0)
        Thread {
            Thread.sleep(1500)
            liveData.postValue(AppState.SuccessLoadWeather(repositoryImpl.getWeatherFromServer()))
        }.start()
    }
}