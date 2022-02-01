package com.skysoft.skyweather.view.citieslist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skysoft.skyweather.repository.RepositoryLocalImpl
import com.skysoft.skyweather.view.AppStateListCities

class ListCitiesViewModel(
    private val liveData: MutableLiveData<AppStateListCities> = MutableLiveData(),
    private val repositoryLocalImpl: RepositoryLocalImpl = RepositoryLocalImpl()

) : ViewModel() {

    fun getLiveData() = liveData

    fun getCitiesList(isRussian: Boolean) {
        liveData.value = AppStateListCities.LoadedCitiesList(
            with(repositoryLocalImpl) {
                if (isRussian) {
                    getRussianCities()
                } else {
                    getWorldCities()
                }
            }
        )
    }
}