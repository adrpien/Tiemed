package com.adrpien.tiemed.settings

import android.os.Bundle
import android.provider.Settings
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.adrpien.tiemed.R
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : PreferenceFragmentCompat() {

    val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val resetPasswordButton: Preference? = findPreference<Preference>(getString(R.string.resetPasswordButton))
        resetPasswordButton.setOnPreferenceClickListener {

            firebaseAuth.sendPasswordResetEmail(firebaseAuth.)

        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        // TODO Settings
    }
}