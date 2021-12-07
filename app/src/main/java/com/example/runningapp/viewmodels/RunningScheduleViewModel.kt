package com.example.runningapp.viewmodels

import androidx.lifecycle.*
import com.example.runningapp.data.RunningScheduleRepository
import com.example.runningapp.data.RunningScheduleEntry
import kotlinx.coroutines.launch
import java.time.LocalDate

class RunningScheduleViewModel(private val repository: RunningScheduleRepository) : ViewModel() {

    val entries: LiveData<List<RunningScheduleEntry>> = repository.runningSchedule.asLiveData()
    val nextRunningDay: LiveData<LocalDate> = repository.nextRunningDay.asLiveData()

    var currentEntry: MutableLiveData<RunningScheduleEntry?> = MutableLiveData(null)


    fun insert(entry: RunningScheduleEntry) = viewModelScope.launch {
        repository.insert(entry)
    }

    fun update(entry: RunningScheduleEntry) = viewModelScope.launch {
        repository.update(entry)

        // update current entry
        if (currentEntry.value?.getId()?.equals(entry.getId()) == true) {
            currentEntry.value = entry
        }
    }

    fun delete(entry: RunningScheduleEntry) = viewModelScope.launch {
        repository.delete(entry)

        // update current entry
        if (currentEntry.value?.getId()?.equals(entry.getId()) == true) {
            currentEntry.value = null
        }
    }
}

class RunningScheduleViewModelFactory(private val repository: RunningScheduleRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RunningScheduleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RunningScheduleViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
