package com.example.activity1

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log

class BackgroundCounterService : Service() {

    private val TAG = "BackgroundCounterService"
    private var isCounting = true
    private var caloriesBurned = 0.0 // Track calories burned
    private val notificationChannelId = "calorie_tracker_channel"
    private var hasSentNotification = false // Track if notification has been sent

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Create notification channel
        createNotificationChannel()

        // Start counting in a background thread
        Thread {
            startCounting()
        }.start()

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        isCounting = false
        Log.d(TAG, "Service stopped")
    }

    private fun startCounting() {
        while (isCounting) {
            caloriesBurned += 0.01 // Increment calories burned
            Log.d(TAG, "Calories burned (not working out): %.2f kcal".format(caloriesBurned))

            // Send notification when 0.3 kcal is reached
            if (caloriesBurned >= 0.3 && !hasSentNotification) {
                sendNotification("You've burned 0.3 kcal by doing nothing!")
                hasSentNotification = true // Mark the notification as sent
            }

            Thread.sleep(1000) // Delay for 1 second
        }
    }

    private fun sendNotification(message: String) {
        val notificationManager = getSystemService(NotificationManager::class.java)
        val notification = Notification.Builder(this, notificationChannelId)
            .setContentTitle("Calorie Tracker")
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Replace with your app's icon
            .setPriority(Notification.PRIORITY_HIGH)
            .build()

        notificationManager.notify(1, notification)
        Log.d(TAG, "Notification sent: $message")
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationChannelId,
                "Calorie Tracker Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for calorie tracking"
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
