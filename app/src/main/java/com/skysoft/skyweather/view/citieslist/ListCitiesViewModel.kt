package com.skysoft.skyweather.view.citieslist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skysoft.skyweather.model.CitiesRepositoryImpl
import com.skysoft.skyweather.view.AppState

class ListCitiesViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: CitiesRepositoryImpl = CitiesRepositoryImpl()

) : ViewModel() {

    fun getLiveData() = liveData

    fun getCitiesList(isRussian: Boolean) {
        liveData.value = AppState.LoadedCitiesList(
            with(repositoryImpl) {
                if (isRussian) {
                    getRussianCities()
                } else {
                    getWorldCities()
                }
            }
        )
    }
}