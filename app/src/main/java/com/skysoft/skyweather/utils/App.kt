package com.skysoft.skyweather.utils

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.GsonBuilder
import com.skysoft.skyweather.model.WeatherApi
import com.skysoft.skyweather.model.YANDEX_API_URL
import com.skysoft.skyweather.room.HistoryDatabase
import com.skysoft.skyweather.room.HistoryWeatherDao
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class App : Application() {

    private val retrofit = Retrofit.Builder()
        .baseUrl(YANDEX_API_URL)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .build().create(WeatherApi::class.java)

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    fun getRetrofit(): WeatherApi? {
        return retrofit
    }

    companion object {
        private var appInstance: App? = null
        val DB_NAME = "History.db"
        private var db: HistoryDatabase? = null

        fun getAppInstance(): App {
            if (appInstance == null)
                throw IllformedLocaleException("Приложение не работает!")
            return appInstance!!
        }

        fun getHistoryWeatherDao(): HistoryWeatherDao {
            if (db == null) {
                db = Room.databaseBuilder(getAppInstance(), HistoryDatabase::class.java, DB_NAME)
                    //.allowMainThreadQueries()
                    .addMigrations(object : Migration(1, 2) {
                        override fun migrate(database: SupportSQLiteDatabase) {
                            database.execSQL("ALTER TABLE history_weather_entity ADD COLUMN dataDate TEXT NOT NULL DEFAULT ''")
                        }
                    })
                    .build()
            }
            return db!!.historyWeatherDao()
        }

    }
}