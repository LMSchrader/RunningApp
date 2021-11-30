package com.example.runningapp.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.runningapp.data.RunHistoryEntry

@RequiresApi(Build.VERSION_CODES.O)
class HistoryViewModel : ViewModel() {

    private val runHistoryEntries: MutableLiveData<List<RunHistoryEntry>> by lazy {
        MutableLiveData<List<RunHistoryEntry>>(loadRunHistoryEntries())
    }

    val currentRunHistoryEntry : MutableLiveData<Int> = MutableLiveData<Int>(0)
    var isInSplitScreenMode : Boolean = false

    fun getRunHistoryEntries(): LiveData<List<RunHistoryEntry>> {
        return runHistoryEntries
    }

    private fun loadRunHistoryEntries(): List<RunHistoryEntry> {
        //TODO implement
        return loadDummyRunHistoryEntries()

        // Do an asynchronous operation to fetch.
    }

    private fun loadDummyRunHistoryEntries(): List<RunHistoryEntry> {
        return RunHistoryEntry.StaticFunctions.getDummyData()
    }
}