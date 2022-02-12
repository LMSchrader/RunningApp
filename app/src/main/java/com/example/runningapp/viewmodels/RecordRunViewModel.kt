package com.example.runningapp.viewmodels

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.runningapp.data.RunHistoryEntryMetaDataWithMeasurements
import com.example.runningapp.data.RunHistoryRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class RecordRunViewModel(private val repository: RunHistoryRepository) : ViewModel() {

    var currentRun: LiveData<RunHistoryEntryMetaDataWithMeasurements?> = MutableLiveData(null)

    fun insertAndObserve(
        entryMetaDataWithMeasurements: RunHistoryEntryMetaDataWithMeasurements,
        lifecycleOwner: LifecycleOwner,
        observerListener: (run: RunHistoryEntryMetaDataWithMeasurements?) -> Unit
    ) =
        viewModelScope.launch {
            repository.insert(entryMetaDataWithMeasurements)
            currentRun.removeObservers(lifecycleOwner)
            currentRun = repository.getAsFlow(entryMetaDataWithMeasurements.metaData.date).asLiveData()
            currentRun.observe(lifecycleOwner, observerListener)
        }

    fun removeObserver(lifecycleOwner: LifecycleOwner) {
        currentRun.removeObservers(lifecycleOwner)
        currentRun = MutableLiveData(null)
    }

    fun setCurrentRunAndObserve(
        id: LocalDateTime, lifecycleOwner: LifecycleOwner,
        observerListener: (run: RunHistoryEntryMetaDataWithMeasurements?) -> Unit
    ) =
        viewModelScope.launch {
            currentRun = repository.getAsFlow(id).asLiveData()
            currentRun.observe(lifecycleOwner, observerListener)
        }
}

class RecordRunViewModelFactory(private val repository: RunHistoryRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecordRunViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecordRunViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}