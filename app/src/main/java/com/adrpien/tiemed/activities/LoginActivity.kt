package com.adrpien.tiemed.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adrpien.tiemed.R
import com.adrpien.tiemed.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    // Instace of Firebase Authentication
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        // If user is already logged, go to MainActivity
        isCurrentUser()
    }

    private fun isCurrentUser() {
        firebaseAuth.currentUser?.let { auth ->

            /*
            FLAG_ACTIVITY_CLEAR_TASK If set in an Intent passed to Context.startActivity(),
            this flag will cause any existing task that would be associated with the activity
            to be cleared before the activity is started.

            FLAG_ACTIVITY_NEW_TASK If set, this activity will become the start of a new task on this history stack.

            Thanks to flags user is not able to go back to LoginActivity
            */
            val intent = Intent(applicationContext, MainActivity::class.java).apply {
                flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            startActivity(intent)
        }
    }
}