package com.example.runningapp.data

import kotlinx.coroutines.flow.Flow

class HomeRepository(runHistoryDao: RunHistoryDao, runningScheduleDao: RunningScheduleDao) {
    val kmRunData: Flow<List<RunHistoryDao.DailyMetaDataTuple>> = runHistoryDao.getKilometersRunForTheLastMonthPerDay()

    val timeRunData: Flow<List<RunHistoryDao.DailyMetaDataTuple>> = runHistoryDao.getTimeRunForTheLastMonthPerDay()

    val averagePaceRunData: Flow<List<RunHistoryDao.DailyMetaDataTuple>> = runHistoryDao.getAveragePaceRunForTheLastMonthPerDay()

    val isTodayARunningDay: Flow<Boolean> = runningScheduleDao.isTodayARunningDay()
}