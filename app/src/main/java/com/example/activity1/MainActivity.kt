package com.example.activity1

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.activity1.QuoteFetchService
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private var quoteService: QuoteFetchService? = null
    private var isServiceBound = false

    private lateinit var tvQuote: TextView

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as QuoteFetchService.QuoteBinder
            quoteService = binder.getService()
            isServiceBound = true
            updateQuote()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            quoteService = null
            isServiceBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create the notification channel
        createNotificationChannel()

        // Set up window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Bind to the QuoteFetchService
        Intent(this, QuoteFetchService::class.java).also {
            bindService(it, serviceConnection, Context.BIND_AUTO_CREATE)
        }

        tvQuote = findViewById(R.id.tvQuote)

        // Initialize buttons
        val btnMainActivity2 = findViewById<MaterialButton>(R.id.btnMainActivity2)
        val btnRecentActivity = findViewById<MaterialButton>(R.id.btnRecentActivity)
        val btnGoals = findViewById<MaterialButton>(R.id.btnGoals)
        val btnMyProgress = findViewById<MaterialButton>(R.id.btnMyProgress)
        val btnLogout = findViewById<MaterialButton>(R.id.btnLogout)

        // Button click listeners
        btnMainActivity2.setOnClickListener {
            startActivity(Intent(this, MainActivity2::class.java))
        }

        btnRecentActivity.setOnClickListener {
            startActivity(Intent(this, RecentActivity::class.java))
        }

        btnGoals.setOnClickListener {
            startActivity(Intent(this, Goals::class.java))
        }

        btnMyProgress.setOnClickListener {
            startActivity(Intent(this, MyProgress::class.java))
        }

        btnLogout.setOnClickListener {
            unbindService()
            FirebaseAuth.getInstance().signOut()
            val serviceIntent = Intent(this, BackgroundCounterService::class.java)
            stopService(serviceIntent)

            // Navigate back to HomeActivity
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    private fun updateQuote() {
        Thread {
            while (isServiceBound) {
                val quoteData = quoteService?.getLatestQuote()
                runOnUiThread {
                    val (quote, author) = quoteData ?: Pair("Fetching quote...", "")
                    tvQuote.text = "\"$quote\"\n- $author"
                }
                Thread.sleep(1000) // Update the quote every second
            }
        }.start()
    }

    private fun unbindService() {
        if (isServiceBound) {
            unbindService(serviceConnection)
            isServiceBound = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "FitTrackChannel",
                "FitTrack Notifications",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Channel for FitTrack activity notifications"
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }
}
