package com.adrpien.tiemed.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.adrpien.tiemed.R
import com.adrpien.tiemed.datamodels.User
import com.adrpien.tiemed.databinding.ActivityMainBinding
import com.adrpien.tiemed.viewmodels.UserViewModel
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var userViewModel: UserViewModel
    private var user: User? = null

    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Creating Bottom Navigation View with Navigation Controller
        binding.bottomNavigationView.setupWithNavController(findNavController(R.id.mainFragmentContainer))

        // Creating instance of ViewModelProvider
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        var userLiveData = userViewModel.getUser()
        user = userLiveData.value

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
            R.id.logOutItem-> {
                firebaseAuth.signOut()
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}