package com.example.activity1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegistrationActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Get references to the input fields and button
        val edtName = findViewById<EditText>(R.id.edtName)
        val edtEmail = findViewById<EditText>(R.id.edtEmail)
        val edtPassword = findViewById<EditText>(R.id.edtPassword)
        val edtConfirmPassword = findViewById<EditText>(R.id.edtConfirmPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val ivBack = findViewById<ImageView>(R.id.ivBack)

        // Set up back arrow click listener
        ivBack.setOnClickListener {
            // Navigate back to the HomeActivity
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        // Set up the register button click listener
        btnRegister.setOnClickListener {
            val name = edtName.text.toString().trim()
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()
            val confirmPassword = edtConfirmPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                registerUser(name, email, password)
            }
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        // Create a new user with email and password using Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // User registration successful
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()

                    // You can now save the user details (e.g., name) to the Firebase database if needed

                    // Redirect to LoginActivity (or any other activity)
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()  // Close the registration activity
                } else {
                    // If registration fails, show an error message
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
