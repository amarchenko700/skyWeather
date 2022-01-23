package com.skysoft.skyweather.view.weathercard

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skysoft.skyweather.R
import com.skysoft.skyweather.model.*
import com.skysoft.skyweather.utils.InternetService
import com.skysoft.skyweather.view.AppState

class WeatherViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData()
) : ViewModel() {

    private var hasInternet = false
    private lateinit var cityToLoad: City

    fun getLiveData(): LiveData<AppState> {
        return liveData
    }

    private fun getWeatherFromServer(city: City, context: Context, timeDelay: Long = 0L) {
        hasInternet = checkForInternet(context)
        cityToLoad = city
        if (!hasInternet) {
            liveData.value = AppState.Error(context.resources.getString(R.string.text_no_internet))
        } else {
            context.startService(Intent(context, InternetService::class.java).apply {
                putExtra(CITY_KEY, city)
            })
        }
    }

    fun getWeather(city: City, context: Context) {
        getWeatherFromServer(city, context, 1)
    }

    fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (it.action == "android.intent.action.AIRPLANE_MODE") {
                it.getBooleanExtra("state", false).let { stateAM ->
                    if (!stateAM) {
                        Toast.makeText(context, "Появился интернет!", Toast.LENGTH_SHORT).show()
                        getWeatherFromServer(cityToLoad, context!!, 15)
                    }
                }
            } else if (it.action == ACTION_ON_LOAD_WEATHER) {
                it.getParcelableExtra<WeatherDTO>(WEATHER_KEY)?.let { weatherDTO ->
                    liveData.value = AppState.SuccessLoadWeather(weatherDTO)
                }

            } else if (it.action == ACTION_ON_ERROR_LOAD_WEATHER) {
                it.getStringExtra(ERROR_KEY)?.let { errorString ->
                    liveData.value = AppState.Error(errorString)
                }
            } else {
            }
        }
    }

    private fun checkForInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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
