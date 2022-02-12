package com.example.runningapp.viewmodels

import androidx.lifecycle.*
import com.example.runningapp.data.HomeRepository
import com.example.runningapp.data.RunHistoryDao

class HomeViewModel(repository: HomeRepository) : ViewModel() {

    val kmRunData: LiveData<List<RunHistoryDao.SummedRunHistoryMetaDataTuple>> = repository.kmRunData.asLiveData()

    val timeRunData: LiveData<List<RunHistoryDao.SummedRunHistoryMetaDataTuple>> = repository.timeRunData.asLiveData()

    val averagePaceRunData: LiveData<List<RunHistoryDao.SummedRunHistoryMetaDataTuple>> = repository.averagePaceRunData.asLiveData()

    val isTodayARunningDay: LiveData<Boolean> = repository.isTodayARunningDay.asLiveData()

}

class HomeViewModelFactory(private val repository: HomeRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}