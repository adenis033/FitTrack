package com.example.activity1

import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.util.*
import kotlin.concurrent.schedule

class Cycling : AppCompatActivity() {

    private var timerRunning = false
    private var timeInSeconds = 0
    private lateinit var handler: Handler
    private lateinit var timerTask: Timer

    private var currentDistance = 0.0 // Track the distance in miles
    private var currentSpeed = 10.0 // Starting speed in mph

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cycling)

        val etCaloriesBurned = findViewById<EditText>(R.id.etCaloriesBurned)
        val etPace = findViewById<EditText>(R.id.etPace)
        val etDistance = findViewById<EditText>(R.id.etDistance)
        val etTime = findViewById<EditText>(R.id.etTime)
        val etSpeed = findViewById<EditText>(R.id.etSpeed)
        val tvTimer = findViewById<TextView>(R.id.tvTimer)
        val btnStartCycling = findViewById<Button>(R.id.btnStartCycling)
        val btnStopCycling = findViewById<Button>(R.id.btnStopCycling)
        val btnSubmitCycling = findViewById<Button>(R.id.btnSubmitCycling)

        // Handler for updating the timer
        handler = Handler(mainLooper)

        // Set initial hints to describe what each value represents
        etCaloriesBurned.setHint("Calories Burned (0 kcal)")
        etPace.setHint("Pace (min/mile)")
        etDistance.setHint("Distance (miles)")
        etSpeed.setHint("Speed (mph)")

        btnStartCycling.setOnClickListener {
            if (!timerRunning) {
                startTimer(tvTimer, etCaloriesBurned, etPace, etDistance, etSpeed)
            }
        }

        btnStopCycling.setOnClickListener {
            if (timerRunning) {
                stopTimer(tvTimer, etTime)
            }
        }

        btnSubmitCycling.setOnClickListener {
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

    private fun startTimer(tvTimer: TextView, etCaloriesBurned: EditText, etPace: EditText, etDistance: EditText, etSpeed: EditText) {
        timerRunning = true
        val formatter = object : TimerTask() {
            override fun run() {
                timeInSeconds++
                val minutes = timeInSeconds / 60
                val seconds = timeInSeconds % 60
                val timeString = String.format("%02d:%02d", minutes, seconds)

                // Update the timer TextView on the main thread
                handler.post {
                    tvTimer.text = timeString
                }

                // Update other values dynamically
                val caloriesBurned = timeInSeconds * 0.2 // 0.2 kcal per second for cycling
                if (timeInSeconds % 10 == 0) {
                    // Add 0.1 mile every 10 seconds
                    currentDistance += 0.1
                }
                if (timeInSeconds % 30 == 0) {
                    // Change speed randomly between 10-20 mph every 30 seconds
                    currentSpeed = (10..20).random().toDouble()
                }

                // Calculate pace (min/mile) as 60 / speed
                val pace = 60 / currentSpeed

                // Update the EditText fields
                handler.post {
                    etCaloriesBurned.setText(caloriesBurned.toString())
                    etPace.setText(String.format("%.2f", pace)) // Display pace with 2 decimal places
                    etDistance.setText(currentDistance.toString())
                    etSpeed.setText(currentSpeed.toString())
                }
            }
        }

        timerTask = Timer()
        timerTask.scheduleAtFixedRate(formatter, 1000, 1000) // Update every 1 second
    }

    private fun stopTimer(tvTimer: TextView, etTime: EditText) {
        timerRunning = false
        timerTask.cancel()

        // Transfer the time value to the time EditText when stopped
        val timeString = tvTimer.text.toString()
        etTime.setText(timeString)

        // Optionally, you can save the time or do any final update here
    }

    private fun saveActivityData(calories: String, pace: String, distance: String, time: String, speed: String) {
        val fileName = "activities.txt"
        val fileContent = """
            Activity: Cycling
            Calories Burned: $calories
            Pace: $pace min/mile
            Distance: $distance miles
            Time: $time
            Speed: $speed mph
            ------
        """.trimIndent()

        try {
            val file = File(filesDir, fileName)
            file.appendText(fileContent + "\n")
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to save activity: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
