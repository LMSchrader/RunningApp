package com.example.runningapp.ui.runningSchedule

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.runningapp.model.RunningScheduleEntry

@RequiresApi(Build.VERSION_CODES.O)
class RunningScheduleViewModel : ViewModel() {

    private val entries: MutableLiveData<List<RunningScheduleEntry>> by lazy {
        MutableLiveData<List<RunningScheduleEntry>>(loadEntries())
    }

    var currentEntry : MutableLiveData<Int> = MutableLiveData<Int>(0)

    fun getEntries(): LiveData<List<RunningScheduleEntry>> {
        return entries
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadEntries(): List<RunningScheduleEntry> {
        //TODO implement
        return loadDummyEntries()

        // Do an asynchronous operation to fetch data.
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadDummyEntries(): List<RunningScheduleEntry> {
        return RunningScheduleEntry.StaticFunctions.getDummyData()
    }

    private fun setCurrentEntry(newIdx : Int) {
        currentEntry.value = newIdx
    }
}