package com.skysoft.skyweather.view.citieslist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skysoft.skyweather.repository.RepositoryImpl
import com.skysoft.skyweather.view.AppStateListCities
import com.skysoft.skyweather.view.AppStateWeather

class ListCitiesViewModel(
    private val liveData: MutableLiveData<AppStateListCities> = MutableLiveData(),
    private val repositoryImpl: RepositoryImpl = RepositoryImpl()

) : ViewModel() {

    fun getLiveData() = liveData

    fun getCitiesList(isRussian: Boolean) {
        liveData.value = AppStateListCities.LoadedCitiesList(
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