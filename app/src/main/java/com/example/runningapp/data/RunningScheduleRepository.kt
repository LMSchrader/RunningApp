package com.example.runningapp.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.LocalDate

class RunningScheduleRepository(private val runningScheduleDao: RunningScheduleDao) {
    val runningSchedule: Flow<List<RunningScheduleEntry>> = runningScheduleDao.getAll()

    val nextRunningDay: Flow<LocalDate> = runningScheduleDao.getNextRunningDay()

    suspend fun insert(entry: RunningScheduleEntry) {
        withContext(Dispatchers.IO) {
            runningScheduleDao.insert(entry)
        }
    }

    suspend fun update(entry: RunningScheduleEntry) {
        withContext(Dispatchers.IO) {
            runningScheduleDao.update(entry)
        }
    }

    suspend fun delete(entry: RunningScheduleEntry) {
        withContext(Dispatchers.IO) {
            runningScheduleDao.delete(entry)
        }
    }
}
