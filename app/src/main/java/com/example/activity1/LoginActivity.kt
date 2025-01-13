package com.example.activity1

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) // Your login XML layout

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        val edtEmail = findViewById<EditText>(R.id.edtEmail)
        val edtPassword = findViewById<EditText>(R.id.edtPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val ivBack = findViewById<ImageView>(R.id.ivBack)

        // Set up back arrow click listener
        ivBack.setOnClickListener {
            // Navigate back to the HomeActivity
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        // Set up login button
        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(email, password)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        // Check if a user is already logged in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Check if the service is already running
            if (!isServiceRunning(this, BackgroundCounterService::class.java)) {
                val serviceIntent = Intent(this, BackgroundCounterService::class.java)
                startService(serviceIntent)
            }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Optional: Close the LoginActivity so the user can't go back
        }
    }

    private fun loginUser(email: String, password: String) {
        // Authenticate with Firebase
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // If login is successful, start the service only if not running
                    if (!isServiceRunning(this, BackgroundCounterService::class.java)) {
                        val serviceIntent = Intent(this, BackgroundCounterService::class.java)
                        startService(serviceIntent)
                    }

                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Optional: Close the login activity
                } else {
                    // If login fails, display a toast message
                    Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Utility method to check if the service is running
    private fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}
