package com.example.runningapp.database

import android.app.Application

class AppApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val runningScheduleRepository by lazy { RunningScheduleRepository(database.runningScheduleDao()) }
    //val runHistoryRepository by lazy { RunHistoryRepository(database.runHistoryDao()) }
}
