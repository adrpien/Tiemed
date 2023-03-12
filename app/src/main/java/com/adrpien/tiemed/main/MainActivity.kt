package com.adrpien.tiemed.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.adrpien.tiemed.R
import com.adrpien.tiemed.databinding.ActivityMainBinding
import com.adrpien.tiemed.presentation.feature_settings.SettingsActivity
import com.adrpien.tiemed.presentation.feature_users.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // ViewBinding
    private lateinit var binding: ActivityMainBinding

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

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
    }

    // Creating Options Menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    // Hanlding options menu click events
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.logOutItem -> {
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
