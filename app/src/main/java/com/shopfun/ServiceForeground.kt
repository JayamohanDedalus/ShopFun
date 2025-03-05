package com.shopfun

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat

class ServiceForeground : Service() {
    private var player: MediaPlayer? = null
    val Notification_Channel_ID: String = "ForegroundServiceChannel"
    val Foreground_ID: Int = 1

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                Notification_Channel_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

//    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification: Notification =
            NotificationCompat.Builder(this, Notification_Channel_ID)
                .setContentTitle("Foreground Service")
                .setContentText("Foreground Service Running...")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Log.d("ServiceForeground", "onStartCommand: startForeground media player")
            startForeground(Foreground_ID, notification, FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK)
        } else {
            Log.d("ServiceForeground", "onStartCommand: startForeground")
            startForeground(Foreground_ID, notification)
        }

        player = MediaPlayer.create(this, Settings.System.DEFAULT_ALARM_ALERT_URI)
        player!!.isLooping = true
        player!!.start()
        return START_STICKY

//        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ServiceForeground", "onDestroy: stop player")
        player?.stop()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}