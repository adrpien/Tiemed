package com.adrpien.tiemed.presentation.feature_settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adrpien.tiemed.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}