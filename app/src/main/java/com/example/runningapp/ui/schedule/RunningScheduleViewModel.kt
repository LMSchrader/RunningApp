package com.example.runningapp.ui.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RunningScheduleViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is running schedule Fragment"
    }
    val text: LiveData<String> = _text
}