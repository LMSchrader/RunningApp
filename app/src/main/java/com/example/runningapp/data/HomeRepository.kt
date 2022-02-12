package com.example.runningapp.data

import kotlinx.coroutines.flow.Flow

class HomeRepository(runHistoryDao: RunHistoryDao, runningScheduleDao: RunningScheduleDao) {
    val kmRunData: Flow<List<RunHistoryDao.SummedRunHistoryMetaDataTuple>> = runHistoryDao.getKilometersRunForTheLastMonth()

    val timeRunData: Flow<List<RunHistoryDao.SummedRunHistoryMetaDataTuple>> = runHistoryDao.getTimeRunForTheLastMonth()

    val averagePaceRunData: Flow<List<RunHistoryDao.SummedRunHistoryMetaDataTuple>> = runHistoryDao.getAveragePaceRunForTheLastMonth()

    val isTodayARunningDay: Flow<Boolean> = runningScheduleDao.isTodayARunningDay()
}