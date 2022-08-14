package com.adrpien.tiemed.settings

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.provider.Settings
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.adrpien.tiemed.R
import com.google.firebase.auth.FirebaseAuth

// TODO SettingsFragment
class SettingsFragment() : PreferenceFragmentCompat() {

    val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val email = firebaseAuth.currentUser?.email
        val resetPasswordButton: Preference? =
            findPreference<Preference>(getString(R.string.resetPasswordButton))
        resetPasswordButton?.setOnPreferenceClickListener {
            if (email != null) {
                firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener {
                    Toast.makeText(
                        context,
                        "Remember to check your spam folder!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            true
        }

        val darkModeButton: SwitchPreference? =
            findPreference<SwitchPreference>(getString(R.string.dark_mode))
        darkModeButton?.setOnPreferenceClickListener {
            if (darkModeButton.isChecked) {
            }
            else {
            }
            true
        }
    }
}