package com.example.runningapp.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class RunHistoryRepository(private val runHistoryDao: RunHistoryDao) {
    val runHistory: Flow<List<RunHistoryEntryMetaDataWithMeasurements>> = runHistoryDao.getAll()

    val kilometersRunForTheLastMonthPerDay: Flow<List<RunHistoryDao.DailyMetaDataTuple>> = runHistoryDao.getKilometersRunForTheLastMonthPerDay()

    val timeRunForTheLastMonthPerDay: Flow<List<RunHistoryDao.DailyMetaDataTuple>> = runHistoryDao.getTimeRunForTheLastMonthPerDay()

    val averagePaceRunForTheLastMonthPerDay: Flow<List<RunHistoryDao.DailyMetaDataTuple>> = runHistoryDao.getAveragePaceRunForTheLastMonthPerDay()

    suspend fun get(entry: LocalDateTime): RunHistoryEntryMetaDataWithMeasurements {
        return withContext(Dispatchers.IO) {
            runHistoryDao.get(entry)
        }
    }

    suspend fun getAsFlow(entry: LocalDateTime): Flow<RunHistoryEntryMetaDataWithMeasurements> {
        return withContext(Dispatchers.IO) {
            runHistoryDao.getAsFlow(entry)
        }
    }

    suspend fun insert(entryMetaDataWithMeasurements: RunHistoryEntryMetaDataWithMeasurements) {
        withContext(Dispatchers.IO) {
            runHistoryDao.insert(entryMetaDataWithMeasurements)
        }
    }

    suspend fun update(entryMetaDataWithMeasurements: RunHistoryEntryMetaDataWithMeasurements) {
        withContext(Dispatchers.IO) {
            runHistoryDao.update(entryMetaDataWithMeasurements)
        }
    }

    suspend fun delete(entryMetaDataWithMeasurements: RunHistoryEntryMetaDataWithMeasurements) {
        withContext(Dispatchers.IO) {
            runHistoryDao.delete(entryMetaDataWithMeasurements)
        }
    }

    suspend fun updateAndInsertLatestMeasurements(entryMetaDataWithMeasurements: RunHistoryEntryMetaDataWithMeasurements, startingIndexOfNewMeasurements : Int) {
        withContext(Dispatchers.IO) {
            runHistoryDao.updateAndInsertLatestMeasurements(entryMetaDataWithMeasurements, startingIndexOfNewMeasurements)
        }
    }
}