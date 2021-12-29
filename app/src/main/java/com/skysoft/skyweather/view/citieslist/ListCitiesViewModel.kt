package com.skysoft.skyweather.view.citieslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skysoft.skyweather.model.WeatherRepositoryImpl
import com.skysoft.skyweather.view.AppState
import java.lang.Thread.sleep

class ListCitiesViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: WeatherRepositoryImpl = WeatherRepositoryImpl()

) : ViewModel() {

    fun getLiveData(): LiveData<AppState> {
        return liveData
    }

    fun getWeatherFromLocalServer(isRussian: Boolean) {
        liveData.value = AppState.Loading(0)
        Thread {
            sleep(2000)
            liveData.postValue(
                AppState.Success(
                    if (isRussian) {
                        repositoryImpl.getWeatherFromLocalStorageRus()
                    } else {
                        repositoryImpl.getWeatherFromLocalStorageWorld()
                    }
                )
            )
        }.start()
    }

    fun getWeatherFromLocalSourceRus() = getWeatherFromLocalServer(true)

    fun getWeatherFromLocalSourceWorld() = getWeatherFromLocalServer(false)

    fun getWeatherFromRemoteSource() = getWeatherFromLocalServer(true)
}