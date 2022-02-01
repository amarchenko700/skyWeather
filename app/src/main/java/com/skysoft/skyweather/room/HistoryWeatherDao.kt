package com.skysoft.skyweather.room

import androidx.room.*

@Dao
interface HistoryWeatherDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: HistoryWeatherEntity)

    @Delete
    fun delete(entity: HistoryWeatherEntity)

    @Update
    fun update(entity: HistoryWeatherEntity)

    @Query("SELECT * FROM history_weather_entity")
    fun getAllHistoryWeather(): List<HistoryWeatherEntity>

    @Query("SELECT * FROM history_weather_entity WHERE cityName IS :cityName")
    fun getHistoryWeather(cityName: String): HistoryWeatherEntity
}