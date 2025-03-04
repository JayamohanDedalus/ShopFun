package com.shopfun

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.widget.Toast

class BroadcastSysEventReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action

        if (WifiManager.WIFI_STATE_CHANGED_ACTION == action)
        {
            val wifiState =
                intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN)
            when (wifiState) {
                WifiManager.WIFI_STATE_ENABLING -> {
                    Toast.makeText(context, "Wi-Fi is ENABLING", Toast.LENGTH_LONG).show()
                }
                WifiManager.WIFI_STATE_DISABLING -> {
                    Toast.makeText(context, "Wi-Fi is DISABLING", Toast.LENGTH_LONG).show()
                }
                WifiManager.WIFI_STATE_ENABLED -> {
                    Toast.makeText(context, "Wi-Fi is ENABLED", Toast.LENGTH_LONG).show()
                }
                WifiManager.WIFI_STATE_DISABLED -> {
                    Toast.makeText(context, "Wi-Fi is DISABLED", Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }

        if (Intent.ACTION_AIRPLANE_MODE_CHANGED == action)
        {
            val airplaneState = intent.getBooleanExtra("state", false)
            when (airplaneState) {
                true -> {
                    Toast.makeText(context, "Airplane Mode is ENABLED", Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(context, "Airplane Mode is DISABLED", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}