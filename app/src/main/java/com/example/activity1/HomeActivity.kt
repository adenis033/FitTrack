package com.example.activity1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        // Find the buttons by their IDs
        val btnGetStarted: Button = findViewById(R.id.btnGetStarted)
        val btnAlreadyAccount: Button = findViewById(R.id.btnAlreadyAccount)

        // Set up listeners for the buttons
        btnGetStarted.setOnClickListener {
            // Navigate to RegistrationActivity
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        btnAlreadyAccount.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
