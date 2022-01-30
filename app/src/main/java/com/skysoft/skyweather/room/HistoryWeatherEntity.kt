package com.skysoft.skyweather.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.DateFormat
import java.util.*

@Entity(tableName = "history_weather_entity")
data class HistoryWeatherEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    var cityName: String = "",
    val temperature: Long = 0,
    val feelsLike: Long = 0,
    val icon: String = "",
    val dataDate: String = DateFormat.getDateInstance().format(Date())
)
