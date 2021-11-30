package com.example.runningapp.data

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class RunningScheduleRepository(private val runningScheduleDao: RunningScheduleDao) {
    val runningSchedule: Flow<List<RunningScheduleEntry>> = runningScheduleDao.getAll()

    //TODO: suspend necessary?
    //@Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(entry: RunningScheduleEntry) {
        runningScheduleDao.insert(entry)
    }

    @WorkerThread
    suspend fun update(entry: RunningScheduleEntry) {
        runningScheduleDao.update(entry)
    }

    @WorkerThread
    suspend fun delete(entry: RunningScheduleEntry) {
        runningScheduleDao.delete(entry)
    }
}
