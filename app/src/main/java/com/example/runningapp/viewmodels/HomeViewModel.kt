package com.example.runningapp.viewmodels

import androidx.lifecycle.*
import com.example.runningapp.data.RunHistoryDao
import com.example.runningapp.data.RunHistoryRepository
import com.example.runningapp.data.RunningScheduleRepository

class HomeViewModel(runningScheduleRepository: RunningScheduleRepository, runHistoryRepository: RunHistoryRepository) : ViewModel() {

    val kilometersRunForTheLastMonthPerDay: LiveData<List<RunHistoryDao.DailyMetaDataTuple>> = runHistoryRepository.kilometersRunForTheLastMonthPerDay.asLiveData()

    val timeRunForTheLastMonthPerDay: LiveData<List<RunHistoryDao.DailyMetaDataTuple>> = runHistoryRepository.timeRunForTheLastMonthPerDay.asLiveData()

    val averagePaceRunForTheLastMonthPerDay: LiveData<List<RunHistoryDao.DailyMetaDataTuple>> = runHistoryRepository.averagePaceRunForTheLastMonthPerDay.asLiveData()

    val isTodayARunningDay: LiveData<Boolean> = runningScheduleRepository.isTodayARunningDay.asLiveData()

}

class HomeViewModelFactory(private val runningScheduleRepository: RunningScheduleRepository, private val runHistoryRepository: RunHistoryRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(runningScheduleRepository, runHistoryRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}