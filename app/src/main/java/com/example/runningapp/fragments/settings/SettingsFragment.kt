package com.example.runningapp.fragments.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.runningapp.R
import com.example.runningapp.worker.RunningNotificationWorker

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == "notifications") {
            val enableNotifications =
                context?.let { PreferenceManager.getDefaultSharedPreferences(it).getBoolean("notifications", true) }
            if(enableNotifications == true) {
                context?.let { RunningNotificationWorker.runAt(it) }
            } else {
                context?.let { RunningNotificationWorker.cancel(it) }
            }
        }
    }
}
