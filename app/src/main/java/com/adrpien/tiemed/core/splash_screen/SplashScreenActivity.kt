package com.adrpien.tiemed.core.splash_screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import com.adrpien.tiemed.databinding.ActivitySplashScreenActivityBinding
import com.adrpien.tiemed.feature_users.presentation.LoginActivity

class  SplashScreenActivity : AppCompatActivity() {

    // ViewBinding
    private lateinit var binding: ActivitySplashScreenActivityBinding

    // Delay duration
    val DELAY_DURATION = 3000L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Setting ViewBinding
        binding = ActivitySplashScreenActivityBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // Hiding supportActionBar
        supportActionBar?.hide()

        /*
        This method can do things like:
        - Dismiss any dialogs the activity was managing.
        - Close any cursors the activity was managing.
        - Close any open search dialog
        Also, onDestroy() isn't a destructor. It doesn't actually destroy the object.
        It's just a method that's called based on a certain state.
        So your instance is still alive and very well* after the superclass's onDestroy() runs and returns.
        Android keeps processes around in case the user wants to restart the app, this makes the startup phase faster.
        The process will not be doing anything and if memory needs to be reclaimed, the process will be killed
         */

        // Setting dark mode
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)

        // Setting time delay
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            val intent = Intent (this, LoginActivity::class.java)
            startActivity(intent)
            finish() // When calling finish() on an activity, the method onDestroy() is executed.

        }, DELAY_DURATION)
    }

}