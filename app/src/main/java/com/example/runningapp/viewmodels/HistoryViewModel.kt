package com.example.runningapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.runningapp.data.RunHistoryEntry
import com.example.runningapp.data.RunHistoryRepository
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: RunHistoryRepository) : ViewModel() {

    val runHistoryEntries: LiveData<List<RunHistoryEntry>> = repository.runHistory.asLiveData()
    //val runHistoryEntriesWithoutCurrentRun: MediatorLiveData<List<RunHistoryEntry>> = MediatorLiveData()

    val currentRunHistoryEntry: MutableLiveData<RunHistoryEntry?> =
        MutableLiveData(null)

    var historyFragmentIsInSplitScreenMode: Boolean = false

    //init {
    //    runHistoryEntriesWithoutCurrentRun.addSource(runHistoryEntriesWithCurrentRun) {
    //        val allRunHistoryEntries: MutableList<RunHistoryEntry> = mutableListOf()
    //        allRunHistoryEntries.addAll(it)
    //        val relevantEntries = allRunHistoryEntries.filter { entry ->
    //            entry.date != currentRun.value?.date
    //        }
    //        runHistoryEntriesWithoutCurrentRun.setValue(relevantEntries)
    //    }
//
    //    runHistoryEntriesWithoutCurrentRun.addSource(currentRun) {
    //        val allRunHistoryEntries = runHistoryEntriesWithCurrentRun.value
    //        val allRunHistoryEntriesCopy: MutableList<RunHistoryEntry> = mutableListOf()
    //        if (allRunHistoryEntries != null) {
    //            allRunHistoryEntriesCopy.addAll(allRunHistoryEntries)
    //        }
    //        val relevantEntries = allRunHistoryEntriesCopy.filter { entry -> entry.date == currentRun.value?.date } //relevantEntries.remove(it)
//
    //        runHistoryEntriesWithoutCurrentRun.setValue(relevantEntries)
    //    }
    //}


    fun insert(entry: RunHistoryEntry) = viewModelScope.launch {
        repository.insert(entry)
    }

    fun update(entry: RunHistoryEntry) = viewModelScope.launch {
        repository.update(entry)
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
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}