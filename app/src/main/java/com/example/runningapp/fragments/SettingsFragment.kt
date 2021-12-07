package com.example.runningapp.fragments

import android.content.Context
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.runningapp.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)


        //val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        //val name = sharedPreferences.getString("notification", "")
    }
}
