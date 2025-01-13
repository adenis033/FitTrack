package com.example.activity1

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import java.io.File
import java.util.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class Running : AppCompatActivity() {

    private var timerRunning = false
    private var timeInSeconds = 0
    private lateinit var handler: Handler
    private lateinit var timerTask: Timer

    private var currentDistance = 0.0 // Distance in miles
    private var currentSpeed = 3.0 // Initial speed in mph

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_running)

        // UI Elements
        val etCaloriesBurned = findViewById<EditText>(R.id.etCaloriesBurned)
        val etPace = findViewById<EditText>(R.id.etPace)
        val etDistance = findViewById<EditText>(R.id.etDistance)
        val etTime = findViewById<EditText>(R.id.etTime)
        val etSpeed = findViewById<EditText>(R.id.etSpeed)
        val tvTimer = findViewById<TextView>(R.id.tvTimer)
        val btnStartRun = findViewById<Button>(R.id.btnStartRun)
        val btnStopRun = findViewById<Button>(R.id.btnStopRun)
        val btnSubmitRunning = findViewById<Button>(R.id.btnSubmitRunning)

        handler = Handler(mainLooper)

        // Request notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                101
            )
        }

        // Start Run Button
        btnStartRun.setOnClickListener {
            if (!timerRunning) {
                // Start the foreground service
                val intent = Intent(this, ForegroundCounterService::class.java)
                startService(intent)

                // Start the timer logic
                startTimer(tvTimer, etCaloriesBurned, etPace, etDistance, etSpeed)
            }
        }

        // Stop Run Button
        btnStopRun.setOnClickListener {
            if (timerRunning) {
                // Stop the foreground service
                val intent = Intent(this, ForegroundCounterService::class.java)
                stopService(intent)

                // Stop the timer logic
                stopTimer(tvTimer, etTime)
            }
        }

        // Submit Run Data Button
        btnSubmitRunning.setOnClickListener {
            val calories = etCaloriesBurned.text.toString().trim()
            val pace = etPace.text.toString().trim()
            val distance = etDistance.text.toString().trim()
            val time = etTime.text.toString().trim()
            val speed = etSpeed.text.toString().trim()

            if (calories.isEmpty() || pace.isEmpty() || distance.isEmpty() || time.isEmpty() || speed.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields!", Toast.LENGTH_SHORT).show()
            } else {
                saveActivityData(calories, pace, distance, time, speed)
                Toast.makeText(this, "Activity Saved!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun getCurrentUserKey(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.uid ?: "default_user" // Use "default_user" as fallback for unauthenticated users
    }

    private fun startTimer(
        tvTimer: TextView,
        etCaloriesBurned: EditText,
        etPace: EditText,
        etDistance: EditText,
        etSpeed: EditText
    ) {
        timerRunning = true
        val formatter = object : TimerTask() {
            override fun run() {
                timeInSeconds++
                val minutes = timeInSeconds / 60
                val seconds = timeInSeconds % 60
                val timeString = String.format("%02d:%02d", minutes, seconds)

                handler.post {
                    tvTimer.text = timeString
                }

                // Update values dynamically
                val caloriesBurned = timeInSeconds * 0.1
                if (timeInSeconds % 10 == 0) {
                    currentDistance += 0.1
                }
                if (timeInSeconds % 30 == 0) {
                    currentSpeed = (3..7).random().toDouble()
                }
                val pace = 60 / currentSpeed

                handler.post {
                    etCaloriesBurned.setText(String.format("%.1f", caloriesBurned))
                    etPace.setText(String.format("%.2f", pace))
                    etDistance.setText(String.format("%.2f", currentDistance))
                    etSpeed.setText(String.format("%.2f", currentSpeed))
                }
            }
        }
        timerTask = Timer()
        timerTask.scheduleAtFixedRate(formatter, 1000, 1000)
    }

    private fun stopTimer(tvTimer: TextView, etTime: EditText) {
        timerRunning = false
        timerTask.cancel()
        etTime.setText(tvTimer.text.toString())
    }

    private fun saveActivityData(
        calories: String,
        pace: String,
        distance: String,
        time: String,
        speed: String
    ) {
        val userKey = getCurrentUserKey()
        val sharedPreferences = getSharedPreferences("RecentActivities", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val gson = Gson()
        val type = object : TypeToken<MutableList<String>>() {}.type
        val existingActivitiesJson = sharedPreferences.getString(userKey, "[]")
        val activities: MutableList<String> = gson.fromJson(existingActivitiesJson, type)

        val newActivity = """
        Activity: Running
        Calories Burned: $calories
        Pace: $pace min/mile
        Distance: $distance miles
        Time: $time
        Speed: $speed mph
    """.trimIndent()

        activities.add(newActivity)
        editor.putString(userKey, gson.toJson(activities))
        editor.apply()
    }
}
