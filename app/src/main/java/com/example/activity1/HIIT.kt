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

class HIIT : AppCompatActivity() {

    private var timerRunning = false
    private var timeInSeconds = 0
    private lateinit var handler: Handler
    private lateinit var timerTask: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hiit)

        val etCaloriesBurned = findViewById<EditText>(R.id.etCaloriesBurned)
        val etIntensity = findViewById<EditText>(R.id.etIntensity)
        val etTime = findViewById<EditText>(R.id.etTime)
        val tvTimer = findViewById<TextView>(R.id.tvTimer)
        val btnStartHIIT = findViewById<Button>(R.id.btnStartHIIT)
        val btnStopHIIT = findViewById<Button>(R.id.btnStopHIIT)
        val btnSubmitHIIT = findViewById<Button>(R.id.btnSubmitHIIT)

        // Handler for updating the timer
        handler = Handler(mainLooper)

        // Set hints to describe each field
        etCaloriesBurned.setHint("Calories Burned")
        etIntensity.setHint("Enter Intensity (1-10)")

        btnStartHIIT.setOnClickListener {
            if (!timerRunning) {
                startTimer(tvTimer, etCaloriesBurned, etIntensity)
            }
        }

        btnStopHIIT.setOnClickListener {
            if (timerRunning) {
                stopTimer(tvTimer, etTime)
            }
        }

        btnSubmitHIIT.setOnClickListener {
            val calories = etCaloriesBurned.text.toString().trim()
            val intensity = etIntensity.text.toString().trim()
            val time = etTime.text.toString().trim()

            if (calories.isEmpty() || intensity.isEmpty() || time.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields!", Toast.LENGTH_SHORT).show()
            } else {
                saveActivityData(calories, intensity, time)
                Toast.makeText(this, "Activity Saved!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun startTimer(tvTimer: TextView, etCaloriesBurned: EditText, etIntensity: EditText) {
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

                // Update calories burned based on time and intensity
                val intensityValue = etIntensity.text.toString().toDoubleOrNull() ?: 1.0 // Default to 1 if no valid intensity is entered
                val caloriesBurned = timeInSeconds * 0.15 * intensityValue // 0.15 kcal per second for HIIT, adjusted by intensity

                // Update the EditText fields
                handler.post {
                    etCaloriesBurned.setText(caloriesBurned.toString())
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
    }

    private fun saveActivityData(calories: String, intensity: String, time: String) {
        val fileName = "activities.txt"
        val fileContent = """
            Activity: HIIT
            Calories Burned: $calories
            Intensity: $intensity
            Time: $time
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
