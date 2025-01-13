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

class Yoga : AppCompatActivity() {

    private var timerRunning = false
    private var timeInSeconds = 0
    private lateinit var handler: Handler
    private lateinit var timerTask: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yoga)

        val etCaloriesBurned = findViewById<EditText>(R.id.etCaloriesBurned)
        val etReps = findViewById<EditText>(R.id.etReps)
        val etSets = findViewById<EditText>(R.id.etSets)
        val etTime = findViewById<EditText>(R.id.etTime)
        val tvTimer = findViewById<TextView>(R.id.tvTimer)
        val btnStartYoga = findViewById<Button>(R.id.btnStartYoga)
        val btnStopYoga = findViewById<Button>(R.id.btnStopYoga)
        val btnSubmitYoga = findViewById<Button>(R.id.btnSubmitYoga)

        // Handler for updating the timer
        handler = Handler(mainLooper)

        // Set hints to describe each field
        etCaloriesBurned.setHint("Calories Burned")
        etReps.setHint("Enter AVG Reps")
        etSets.setHint("Enter AVG Sets")

        btnStartYoga.setOnClickListener {
            if (!timerRunning) {
                startTimer(tvTimer, etCaloriesBurned, etReps, etSets)
            }
        }

        btnStopYoga.setOnClickListener {
            if (timerRunning) {
                stopTimer(tvTimer, etTime)
            }
        }

        btnSubmitYoga.setOnClickListener {
            val calories = etCaloriesBurned.text.toString().trim()
            val reps = etReps.text.toString().trim()
            val sets = etSets.text.toString().trim()
            val time = etTime.text.toString().trim()

            if (calories.isEmpty() || reps.isEmpty() || sets.isEmpty() || time.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields!", Toast.LENGTH_SHORT).show()
            } else {
                saveActivityData(calories, reps, sets, time)
                Toast.makeText(this, "Activity Saved!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun startTimer(tvTimer: TextView, etCaloriesBurned: EditText, etReps: EditText, etSets: EditText) {
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

                // Update calories burned based on time
                val caloriesBurned = timeInSeconds * 0.05 // 0.05 kcal per second for yoga

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

    private fun saveActivityData(calories: String, reps: String, sets: String, time: String) {
        val fileName = "activities.txt"
        val fileContent = """
            Activity: Yoga
            Calories Burned: $calories
            AVG Reps: $reps reps
            AVG Sets: $sets sets
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
