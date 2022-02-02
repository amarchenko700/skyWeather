package com.skysoft.skyweather.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(HistoryWeatherEntity::class), version = 2, exportSchema = false)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun historyWeatherDao(): HistoryWeatherDao
}