package com.skysoft.skyweather.utils

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.skysoft.skyweather.R
import com.skysoft.skyweather.model.*
import com.skysoft.skyweather.repository.RepositoryRemoteImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InternetService(name: String = "") : IntentService(name) {

    private val callback = object : Callback<WeatherDTO> {
        override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
            if (response.isSuccessful) {
                response.body()?.let {
                    sendBroadcast(Intent(ACTION_ON_LOAD_WEATHER).apply {
                        this.putExtra(WEATHER_KEY, it)
                    })
                }
            }
        }

        override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
            sendBroadcast(Intent(ACTION_ON_ERROR_LOAD_WEATHER).apply {
                this.putExtra(ERROR_KEY, t.toString())
            })
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        if (hasInternet()) {
            intent?.let { intent ->
                intent.getParcelableExtra<City>(CITY_KEY)?.let { city ->
                    RepositoryRemoteImpl().getWeatherFromServer(city, callback)
                }
            }
        } else {
            applicationContext.sendBroadcast(Intent(ACTION_ON_ERROR_NO_INTERNET).apply {
                this.putExtra(ERROR_KEY, resources.getString(R.string.text_no_internet))
            })
        }
    }

    private fun hasInternet(): Boolean {
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }

}