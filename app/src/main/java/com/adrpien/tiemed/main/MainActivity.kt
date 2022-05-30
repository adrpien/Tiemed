package com.adrpien.tiemed.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.adrpien.tiemed.R
import com.adrpien.tiemed.datamodels.users.User
import com.adrpien.tiemed.databinding.ActivityMainBinding
import com.adrpien.tiemed.settings.SettingsActivity
import com.adrpien.tiemed.viewmodels.UserViewModel
import com.google.firebase.auth.FirebaseAuth



class MainActivity : AppCompatActivity() {

    // ViewBinding
    private lateinit var binding: ActivityMainBinding

    // ViewModel
    val userViewModel by viewModels<UserViewModel>()

    // Firebase Authentication required to log out
    private val firebaseAuth = FirebaseAuth.getInstance()

    // onCreate lifecycle function
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Creating Bottom Navigation View with Navigation Controller
        binding.bottomNavigationView.setupWithNavController(findNavController(R.id.mainFragmentContainer))
    }

    // Creating Options Menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        return true
    }

    // Hanlding options menu click events
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.logOutItem -> {

                // TODO logoutMenuItem click reaction
                firebaseAuth.signOut()
                finish()
                true
            }
            R.id.settingsItem -> {

                // settingMenuItem click reaction
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.aboutItem -> {
                // TODO aboutMenuItem click reaction
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }
}
