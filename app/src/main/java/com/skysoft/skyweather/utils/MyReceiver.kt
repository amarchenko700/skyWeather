package com.skysoft.skyweather.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.net.ConnectivityManagerCompat

class MyReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (!intent?.getBooleanExtra("state", false)!!)
            Toast.makeText(context, "Появился интернет!", Toast.LENGTH_SHORT).show()
    }
}