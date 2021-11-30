package com.example.runningapp

import android.app.Application
import com.example.runningapp.data.AppDatabase
import com.example.runningapp.data.RunningScheduleRepository

class AppApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val runningScheduleRepository by lazy { RunningScheduleRepository(database.runningScheduleDao()) }
    //val runHistoryRepository by lazy { RunHistoryRepository(database.runHistoryDao()) }
}
