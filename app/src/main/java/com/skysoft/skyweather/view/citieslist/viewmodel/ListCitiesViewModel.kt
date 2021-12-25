package com.skysoft.skyweather.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skysoft.skyweather.model.City
import com.skysoft.skyweather.model.WeatherRepositoryImpl
import com.skysoft.skyweather.view.citieslist.viewmodel.AppState
import java.lang.Thread.sleep

class ListCitiesViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: WeatherRepositoryImpl = WeatherRepositoryImpl()
) : ViewModel() {

    fun getLiveData(): LiveData<AppState> {
        return liveData
    }

    fun getWeatherFromServer() {
        liveData.value = AppState.Loading(5)
        Thread {
            sleep(3000)
            //liveData.postValue(AppState.Error(IllegalStateException("")))
            val rand = (-10..20).random()
            liveData.postValue(AppState.Success(repositoryImpl.getWeatherFromServer()))
        }.start()
    }

    fun openCityCard(city: City?) {
        liveData.value = AppState.openCityCard(city!!)
    }
}