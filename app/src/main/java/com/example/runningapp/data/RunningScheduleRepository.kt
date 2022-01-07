package com.example.runningapp.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class RunningScheduleRepository(private val runningScheduleDao: RunningScheduleDao) {
    val runningSchedule: Flow<List<RunningScheduleEntry>> = runningScheduleDao.getAll()

    //val nextRunningDay: Flow<LocalDate> = runningScheduleDao.getNextRunningDay()

    val isTodayARunningDay: Flow<Boolean> = runningScheduleDao.isTodayARunningDay()

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

    //suspend fun isTodayARunningDay(mon: Boolean, tue: Boolean, wed: Boolean, thu: Boolean, fri: Boolean, sat: Boolean, sun: Boolean) {
    //    withContext(Dispatchers.IO) {
    //        runningScheduleDao.isTodayARunningDay(mon, tue, wed, thu, fri, sat, sun)
//
    //    }
    //}
}
