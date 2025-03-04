package com.shopfun

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.provider.Settings
import androidx.compose.ui.text.font.FontVariation

class ServiceBackground : Service() {

    private var player: MediaPlayer? = null

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        player = MediaPlayer.create(this, Settings.System.DEFAULT_NOTIFICATION_URI)
        player!!.isLooping = true
        player!!.start()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.stop()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}