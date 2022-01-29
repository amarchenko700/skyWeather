package com.skysoft.skyweather.view.contacts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skysoft.skyweather.view.AppStateContacts

class ContactsViewModel(
    private val liveData: MutableLiveData<AppStateContacts> = MutableLiveData()
) : ViewModel() {

    fun getLiveData() = liveData

}