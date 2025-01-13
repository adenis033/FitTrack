package com.example.activity1

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth


class RecentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recent)

        val listView: ListView = findViewById(R.id.listViewRecentActivities)

        // Load activities from Shared Preferences
        val activities = loadActivitiesFromPreferences()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, activities)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedActivity = activities[position]
            Toast.makeText(this, "Selected: $selectedActivity", Toast.LENGTH_SHORT).show()
        }

        val btnDeleteRecentActivities = findViewById<MaterialButton>(R.id.btnDeleteRecentActivities)
        btnDeleteRecentActivities.setOnClickListener {
            deleteRecentActivities()
            val updatedActivities = loadActivitiesFromPreferences()
            val updatedAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, updatedActivities)
            listView.adapter = updatedAdapter
        }
    }

    private fun getCurrentUserKey(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.uid ?: "default_user" // Use "default_user" as fallback for unauthenticated users
    }

    private fun loadActivitiesFromPreferences(): List<String> {
        val userKey = getCurrentUserKey()
        val sharedPreferences = getSharedPreferences("RecentActivities", MODE_PRIVATE)
        val gson = Gson()
        val type = object : TypeToken<List<String>>() {}.type
        val activitiesJson = sharedPreferences.getString(userKey, "[]")

        return gson.fromJson(activitiesJson, type) ?: emptyList()
    }


    private fun deleteRecentActivities() {
        val userKey = getCurrentUserKey()
        val sharedPreferences = getSharedPreferences("RecentActivities", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(userKey, "[]") // Clear activities for the current user
        editor.apply()

        Toast.makeText(this, "Recent activities deleted", Toast.LENGTH_SHORT).show()
    }

}
