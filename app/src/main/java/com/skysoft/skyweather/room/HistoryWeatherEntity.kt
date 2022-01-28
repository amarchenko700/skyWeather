package com.skysoft.skyweather.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_weather_entity")
data class HistoryWeatherEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    var cityName: String = "",
    val temperature: Long = 0,
    val feelsLike: Long = 0,
    val icon: String = ""
)
