package com.example.runningapp.fragments.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.runningapp.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)


        //val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        //val name = sharedPreferences.getString("notification", "")
    }
}
