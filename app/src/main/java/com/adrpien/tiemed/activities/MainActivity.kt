package com.adrpien.tiemed.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adrpien.tiemed.R
import com.adrpien.tiemed.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}