package com.adrpien.tiemed.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import com.adrpien.tiemed.R
import com.adrpien.tiemed.databinding.ActivitySplashScreenActivityBinding

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setting ViewBinding
        binding = ActivitySplashScreenActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hiding supportActionBar
        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            val intent = Intent (this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }

}