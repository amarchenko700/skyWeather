package com.skysoft.skyweather.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weather(
    val cityName: String = "Москва",
    val temperature: Long = 20,
    val feelsLike: Long = 20,
    val icon: String = "skc_n"
) : Parcelable