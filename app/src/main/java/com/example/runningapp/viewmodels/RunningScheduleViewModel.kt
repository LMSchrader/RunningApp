package com.example.runningapp.viewmodels

import androidx.lifecycle.*
import com.example.runningapp.data.RunningScheduleRepository
import com.example.runningapp.data.RunningScheduleEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RunningScheduleViewModel(private val repository: RunningScheduleRepository) : ViewModel() {

    val entries: LiveData<List<RunningScheduleEntry>> = repository.runningSchedule.asLiveData()

    var currentEntry : MutableLiveData<Int> = MutableLiveData<Int>(0)

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(entry: RunningScheduleEntry) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            repository.insert(entry)
        }
    }
}

class RunningScheduleViewModelFactory(private val repository: RunningScheduleRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RunningScheduleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RunningScheduleViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
