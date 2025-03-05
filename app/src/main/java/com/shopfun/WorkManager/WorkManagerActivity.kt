package com.shopfun.WorkManager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.shopfun.WorkManager.ui.theme.ShopFunTheme
import java.util.concurrent.TimeUnit

class WorkManagerActivity : ComponentActivity() {
    private val Notification_Channel_ID: String = "WorkManagerChannel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101
                )
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(Notification_Channel_ID, Notification_Channel_ID, NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

/*
        Test Observation:
        OneTimeWorkRequest -
        1. OneTime button works with notification only once per app execution i.e. first time onClick.
            Probably the objects should set to "remember by mutable".
        2. When application is not active, Notification is received. But only for first time click.
        3. When application is closed, Notification is received. But only for first time click.
        4. When device is restarted, Notification is received only when app is opened after device restart. But only for first time click.
*/

        val oneTimeWorkRequest = OneTimeWorkRequestBuilder<WorkerEvent>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresCharging(true)
                    .build()
                )
            .setInputData(Data.Builder().putString("TaskMode", "OneTime").build())
            .setInitialDelay(60, TimeUnit.SECONDS)
            .build()

        val interval : Long = 10
        val periodicWorkRequest = PeriodicWorkRequestBuilder<WorkerEvent>(interval, TimeUnit.SECONDS)
            .setInputData(Data.Builder()
                .putString("TaskMode", "Periodic")
                .putLong("Interval", interval)
                .build())
            .build()

        val Chain1oneTimeWorkRequest = OneTimeWorkRequestBuilder<WorkerEvent>()
            .setInputData(Data.Builder()
                .putString("TaskMode", "OneTime")
                .putString("TaskTitle", "Chain1")
                .build())
            .build()

        val Chain2oneTimeWorkRequest = OneTimeWorkRequestBuilder<WorkerEvent>()
            .setInitialDelay(10, TimeUnit.SECONDS)
            .setInputData(Data.Builder()
                .putString("TaskMode", "OneTime")
                .putString("TaskTitle", "Chain2")
                .build())
            .build()

        /*
                                .setConstraints(
                                    Constraints.Builder()
                                        .setRequiredNetworkType(NetworkType.UNMETERED) // Only use Wi-Fi for downloading
                                        .setRequiresCharging(true) // Only run when charging
                                        .build()
                                )
        */


        setContent {

            ShopFunTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column (modifier = Modifier.padding(innerPadding)){
                        Button(
                            onClick = {
                                WorkManager.getInstance(applicationContext).enqueue(oneTimeWorkRequest)
                            }
                        )
                        {
                            Text(text = "oneTime Work Request")
                        }

                        Button(
                            onClick = {
                                WorkManager.getInstance(applicationContext).enqueue(periodicWorkRequest)
                            }
                        )
                        {
                            Text(text = "Periodic Work Request")
                        }

                        Button(
                            onClick = {
                                WorkManager.getInstance(applicationContext)
                                    .beginWith(Chain1oneTimeWorkRequest)
                                    .then(Chain2oneTimeWorkRequest)
                                    .enqueue()
                            }
                        )
                        {
                            Text(text = "Chained Work Request")
                        }
                    }
                }
            }
        }
    }
}
