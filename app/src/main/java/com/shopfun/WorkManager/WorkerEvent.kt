package com.shopfun.WorkManager

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.shopfun.R

class WorkerEvent(context: Context, workerParameters: WorkerParameters): Worker(context, workerParameters) {

    private val Notification_Channel_ID: String = "WorkManagerChannel"

    override fun doWork(): Result {
        try {
            val taskMode = inputData.getString("TaskMode") ?: "Default"
            val taskTitle = inputData.getString("TaskTitle") ?: ""
            var interval : Long = inputData.getLong("Interval", defaultValue = 0)
            var intervalString : String = ""
            if (interval != 0.toLong()) {
                interval += (interval - 10)
                intervalString = " - Periodic Cycle: ${interval.toString()} Seconds"
            } else {}
            val msgWrkEvent: String = "$taskMode Work Request ($taskTitle) Called and Process Completed $intervalString"

            Log.d("WorkerEvent", msgWrkEvent)

            // Send a notification to the user when work is done
            sendNotification(taskMode, msgWrkEvent)

            return Result.success()
        } catch (exception: Exception) {
            return Result.failure()
        }
    }

    override fun onStopped() {
        super.onStopped()
    }

    @SuppressLint("MissingPermission")
    private fun sendNotification(taskMode: String, msgWrkEvent: String) {

        val notification = NotificationCompat.Builder(applicationContext, Notification_Channel_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("$taskMode - Task")
            .setContentText(msgWrkEvent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        // Perform the background task here, such as displaying a notification
        NotificationManagerCompat.from(applicationContext).notify(1, notification)
    }
}


