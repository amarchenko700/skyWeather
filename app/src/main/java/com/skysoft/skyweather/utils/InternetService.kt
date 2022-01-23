package com.skysoft.skyweather.utils

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.skysoft.skyweather.model.City

class InternetService(name: String = "") : IntentService(name) {

    override fun onHandleIntent(intent: Intent?) {

    }

    private fun getWeatherFromServer(city: City, context: Context) {

    }
}