package com.example.activity1

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.activity1.R

class ForegroundCounterService : Service() {

    private val TAG = "ForegroundCounterService"
    private var isCounting = true
    private var secondsElapsed = 0
    private val channelId = "foreground_service_channel"

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Create notification channel and start foreground service
        createNotificationChannel()
        startForeground(1, createNotification("00:00:00"))
        Thread {
            startCountingThread()
        }.start()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        isCounting = false
        Log.d(TAG, "Foreground service stopped")
    }

    private fun startCountingThread() {
        while (isCounting) {
            secondsElapsed++
            val timeFormatted = formatTime(secondsElapsed)
            updateNotification(timeFormatted)
            Log.d(TAG, "Time elapsed: $timeFormatted")
            Thread.sleep(1000) // Update every second
        }
    }

    private fun formatTime(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, secs)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            channelId,
            "Foreground Counter Service",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    private fun createNotification(time: String): Notification {
        return Notification.Builder(this, channelId)
            .setContentTitle("You've been working out for: ")
            .setContentText(time)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setVisibility(Notification.VISIBILITY_PUBLIC) // Ensure visibility
            .build()
    }

    private fun updateNotification(time: String) {
        val manager = getSystemService(NotificationManager::class.java)
        val notification = createNotification(time)
        manager.notify(1, notification)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
