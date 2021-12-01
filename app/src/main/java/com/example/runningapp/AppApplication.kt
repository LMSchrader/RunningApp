package com.example.runningapp

import android.app.Application
import com.example.runningapp.data.AppDatabase
import com.example.runningapp.data.RunHistoryRepository
import com.example.runningapp.data.RunningScheduleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class AppApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val runningScheduleRepository by lazy { RunningScheduleRepository(database.runningScheduleDao()) }
    val runHistoryRepository by lazy { RunHistoryRepository(database.runHistoryDao()) }
}
