package com.skysoft.skyweather.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skysoft.skyweather.model.City
import com.skysoft.skyweather.model.WeatherRepositoryImpl
import com.skysoft.skyweather.view.AppState
import java.lang.Thread.sleep

class ListCitiesViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
) : ViewModel() {

    fun getLiveData(): LiveData<AppState> {
        return liveData
    }

    fun openCityCard(city: City?) {
        liveData.value = AppState.OpenCityCard(city!!)
    }

    fun getCities() {
        liveData.value = AppState.LoadingCities(0)
        Thread {
            sleep(2000)
            liveData.postValue(AppState.SuccessLoadCities)
        }.start()
    }
}