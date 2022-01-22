package com.skysoft.skyweather.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log

class HasInternet: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("myLogs", "air plane mode: " + isAirplaneModeOn(context!!).toString())
    }

    private fun isAirplaneModeOn(context: Context): Boolean {
        return Settings.System.getInt(
            context.contentResolver,
            Settings.Global.AIRPLANE_MODE_ON, 0
        ) != 0
    }
}