package com.example.runningapp.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class RunHistoryRepository(private val runHistoryDao: RunHistoryDao) {
    val runHistory: Flow<List<RunHistoryEntry>> = runHistoryDao.getAll()

    suspend fun insert(entry: RunHistoryEntry) {
        withContext(Dispatchers.IO) {
            runHistoryDao.insert(entry)
        }
    }

    suspend fun delete(entry: RunHistoryEntry) {
        withContext(Dispatchers.IO) {
            runHistoryDao.delete(entry)
        }
    }
}