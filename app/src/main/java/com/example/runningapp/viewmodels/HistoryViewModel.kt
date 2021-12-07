package com.example.runningapp.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.runningapp.data.RunHistoryEntry
import com.example.runningapp.data.RunHistoryRepository
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class HistoryViewModel(private val repository: RunHistoryRepository) : ViewModel() {

    val runHistoryEntries: LiveData<List<RunHistoryEntry>> = repository.runHistory.asLiveData()

    val currentRunHistoryEntry : MutableLiveData<RunHistoryEntry?> =
        MutableLiveData(null)
    var isInSplitScreenMode : Boolean = false

    fun insert(entry: RunHistoryEntry) = viewModelScope.launch {
            repository.insert(entry)
    }

    fun delete(entry: RunHistoryEntry) = viewModelScope.launch {
            repository.delete(entry)

        // update current entry
        if (currentRunHistoryEntry.value?.date?.equals(entry.date) == true) {
            currentRunHistoryEntry.value = null
        }
    }
}

class HistoryViewModelFactory(private val repository: RunHistoryRepository) :
    ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}