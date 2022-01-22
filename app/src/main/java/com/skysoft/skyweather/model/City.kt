package com.skysoft.skyweather.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

const val CITY_KEY = "CITY_KEY"

@Parcelize
data class City(
    val name: String,
    val latitude: Double,
    val longitude: Double
) : Parcelable