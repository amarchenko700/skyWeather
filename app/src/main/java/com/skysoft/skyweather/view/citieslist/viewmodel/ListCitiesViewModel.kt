package com.skysoft.skyweather.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skysoft.skyweather.model.City
import com.skysoft.skyweather.view.citieslist.viewmodel.AppState
import java.lang.Thread.sleep

class ListCitiesViewModel(private val liveData: MutableLiveData<AppState> = MutableLiveData()) : ViewModel() {

    fun getLiveData(): LiveData<AppState> {
        return liveData
    }

    fun getCityWeather(){
        liveData.value = AppState.Loading(5)
        Thread{
            sleep(3000)
            //liveData.postValue(AppState.Error(IllegalStateException("")))
            val rand = (-10..20).random()
            liveData.postValue(AppState.Success("Температура за бортом: ${rand}"))
        }.start()
    }

    fun openCityCard(city: City?){
        liveData.value = AppState.CityCard(city!!)
    }
}