package com.skysoft.skyweather.view.mainview

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.skysoft.skyweather.R
import java.util.*

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
    }

    override fun onResume() {
        super.onResume()
        Timer().schedule(RemindTask(this), 1000)
    }

    fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        finish()
        startActivity(intent)
    }

    class RemindTask(val splashScreenActivity: SplashScreenActivity) : TimerTask() {
        override fun run() {
            splashScreenActivity.startMainActivity()
        }
    }
}