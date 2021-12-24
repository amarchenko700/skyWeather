package com.skysoft.skyweather.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skysoft.skyweather.viewmodel.AppState
import java.lang.Thread.sleep

class ListCitiesViewModel(private val liveData: MutableLiveData<AppState> = MutableLiveData()) : ViewModel() {

    fun getLiveData(): LiveData<AppState> {
        return liveData
    }

    fun emulateRequest(){
        Thread{
            liveData.postValue(AppState.Loading(0))
            sleep(3000)
            liveData.postValue(AppState.Success("Гололед"))
        }.start()
    }
}