package com.skysoft.skyweather.viewmodel

sealed class AppState {
    data class Loading(var progress: Int) : AppState()
    data class Success(var weatherData: String) : AppState()
    data class Error(var error:Throwable) : AppState()
}