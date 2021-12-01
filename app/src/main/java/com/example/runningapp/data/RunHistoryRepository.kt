package com.example.runningapp.data

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class RunHistoryRepository(private val runHistoryDao: RunHistoryDao) {
    val runHistory: Flow<List<RunHistoryEntry>> = runHistoryDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(entry: RunHistoryEntry) {
        runHistoryDao.insert(entry)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(entry: RunHistoryEntry) {
        runHistoryDao.delete(entry)
    }
}