package com.skysoft.skyweather.view.citieslist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skysoft.skyweather.model.WeatherRepositoryImpl
import com.skysoft.skyweather.view.AppState

class ListCitiesViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: WeatherRepositoryImpl = WeatherRepositoryImpl()

) : ViewModel() {

    fun getLiveData() = liveData

    fun getWeatherFromLocalServer(isRussian: Boolean) {
        liveData.value = AppState.Success(
            with(repositoryImpl) {
                if (isRussian) {
                    getWeatherFromLocalStorageRus()
                } else {
                    getWeatherFromLocalStorageWorld()
                }
            }
        )
    }

    fun getWeatherFromLocalSourceRus() = getWeatherFromLocalServer(true)

    fun getWeatherFromLocalSourceWorld() = getWeatherFromLocalServer(false)

    fun getWeatherFromRemoteSource() = getWeatherFromLocalServer(true)
}