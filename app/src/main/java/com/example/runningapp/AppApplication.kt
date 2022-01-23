package com.example.runningapp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.runningapp.data.AppDatabase
import com.example.runningapp.data.RunHistoryRepository
import com.example.runningapp.data.RunningScheduleRepository

class AppApplication : Application() {
    val shardPref: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            getString(R.string.record_run_preferences_file),
            Context.MODE_PRIVATE
        )
    }
    private val database by lazy { AppDatabase.getDatabase(this) }
    val runningScheduleRepository by lazy { RunningScheduleRepository(database.runningScheduleDao()) }
    val runHistoryRepository by lazy { RunHistoryRepository(database.runHistoryDao()) }
}
