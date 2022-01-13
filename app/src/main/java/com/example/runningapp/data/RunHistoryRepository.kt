package com.example.runningapp.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class RunHistoryRepository(private val runHistoryDao: RunHistoryDao) {
    val runHistory: Flow<List<RunHistoryEntry>> = runHistoryDao.getAll()

    // TODO: klappt das mit return?
    suspend fun get(entry: LocalDateTime): RunHistoryEntry {
       return withContext(Dispatchers.IO) {
            runHistoryDao.get(entry)
        }
    }

    suspend fun getAsFlow(entry: LocalDateTime): Flow<RunHistoryEntry> {
        return withContext(Dispatchers.IO) {
            runHistoryDao.getAsFlow(entry)
        }
    }

    // TODO: klappt das mit return?
    suspend fun insert(entry: RunHistoryEntry): Long {
        return withContext(Dispatchers.IO) {
            runHistoryDao.insert(entry)
        }
    }

    suspend fun update(entry: RunHistoryEntry) {
        withContext(Dispatchers.IO) {
            runHistoryDao.update(entry)
        }
    }

    suspend fun delete(entry: RunHistoryEntry) {
        withContext(Dispatchers.IO) {
            runHistoryDao.delete(entry)
        }
    }
}