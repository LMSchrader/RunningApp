package com.example.runningapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.runningapp.data.RunHistoryEntryMetaDataWithMeasurements
import com.example.runningapp.data.RunHistoryRepository
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: RunHistoryRepository) : ViewModel() {

    val runHistoryEntriesMetaDataWithMeasurements: LiveData<List<RunHistoryEntryMetaDataWithMeasurements>> = repository.runHistory.asLiveData()

    val currentRunHistoryEntryMetaDataWithMeasurements: MutableLiveData<RunHistoryEntryMetaDataWithMeasurements?> =
        MutableLiveData(null)

    var historyFragmentIsInSplitScreenMode: Boolean = false
    var currentRecyclerViewPosition: Int? = null
    var currentViewPagerItem: Int = 0


    fun insert(entryMetaDataWithMeasurements: RunHistoryEntryMetaDataWithMeasurements) = viewModelScope.launch {
        repository.insert(entryMetaDataWithMeasurements)
    }

    fun update(entryMetaDataWithMeasurements: RunHistoryEntryMetaDataWithMeasurements) = viewModelScope.launch {
        repository.update(entryMetaDataWithMeasurements)
    }

    fun delete(entryMetaDataWithMeasurements: RunHistoryEntryMetaDataWithMeasurements) = viewModelScope.launch {
        repository.delete(entryMetaDataWithMeasurements)

        // update current entry
        if (currentRunHistoryEntryMetaDataWithMeasurements.value?.metaData?.date?.equals(entryMetaDataWithMeasurements.metaData.date) == true) {
            currentRunHistoryEntryMetaDataWithMeasurements.value = null
        }
    }
}

class HistoryViewModelFactory(private val repository: RunHistoryRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}