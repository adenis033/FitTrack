package com.example.activity1

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import java.io.File

class MyProgress : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_progress)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //UI components
        val tvCaloriesBurned = findViewById<TextView>(R.id.tvCaloriesBurned)
        val tvTotalTimeSpent = findViewById<TextView>(R.id.tvTotalTimeSpent)
        val tvDistanceCovered = findViewById<TextView>(R.id.tvDistanceCovered)
        val btnRecentActivity = findViewById<MaterialButton>(R.id.btnRecentActivity)

        // Calculate and display the progress summary
        val summary = calculateProgressSummary()
        tvCaloriesBurned.text = "Calories Burned: ${summary.totalCalories} kcal"
        tvTotalTimeSpent.text = "Total Time Spent: ${summary.totalTime} hrs"
        tvDistanceCovered.text = "Distance Covered: ${summary.totalDistance} miles"

        //Recent Activity button
        btnRecentActivity.setOnClickListener {
            val intent = Intent(this, RecentActivity::class.java)
            startActivity(intent)
        }
    }

    // Calculate progress summary by reading activities.txt
    private fun calculateProgressSummary(): ProgressSummary {
        val activityData = loadActivitiesFromFile() // Get lines from the file
        var totalCalories = 0
        var totalTime = 0.0
        var totalDistance = 0.0

        // Process each activity data line
        for (line in activityData) {
            when {
                // Extracting Calories
                line.startsWith("Calories Burned:") -> {
                    val caloriesStr = line.substringAfter("Calories Burned:").trim()
                    val calories = caloriesStr.removeSuffix("kcal").toIntOrNull() ?: 0
                    totalCalories += calories
                }

                // Extracting Time and converting to hours
                line.startsWith("Time:") -> {
                    val timeStr = line.substringAfter("Time:").trim()
                    val timeInHours = parseTimeToHours(timeStr)
                    totalTime += timeInHours
                }

                // Extracting Distance
                line.startsWith("Distance:") -> {
                    val distanceStr = line.substringAfter("Distance:").trim()
                    val distance = distanceStr.removeSuffix("miles").toDoubleOrNull() ?: 0.0
                    totalDistance += distance
                }
            }
        }
        return ProgressSummary(totalCalories, totalTime, totalDistance)
    }


    // Convert time format to total hours
    private fun parseTimeToHours(time: String): Double {
        val parts = time.split(":").map { it.toIntOrNull() ?: 0 }
        val hours = parts.getOrNull(0) ?: 0
        val minutes = parts.getOrNull(1) ?: 0
        val seconds = parts.getOrNull(2) ?: 0
        return hours + (minutes / 60.0) + (seconds / 3600.0)
    }

    // Load activities from the activities.txt file
    private fun loadActivitiesFromFile(): List<String> {
        val fileName = "activities.txt"
        val file = File(filesDir, fileName)

        return if (file.exists()) {
            file.readLines() // Read the lines from the file
        } else {
            Toast.makeText(this, "No recent activities found.", Toast.LENGTH_SHORT).show()
            emptyList()
        }
    }
}

// Data class to hold overall progress summary
data class ProgressSummary(
    val totalCalories: Int,
    val totalTime: Double,
    val totalDistance: Double
)

