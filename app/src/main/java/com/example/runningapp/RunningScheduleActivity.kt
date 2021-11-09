package com.example.runningapp

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.runningapp.databinding.ActivityRunningScheduleBinding
import com.example.runningapp.model.RunningScheduleEntry

class RunningScheduleActivity : Activity() {
    private lateinit var binding: ActivityRunningScheduleBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRunningScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = RunningScheduleEntry.StaticFunctions.getDummyData()
        binding.title.text = data.getTitle()
        val date = data.getStartDate().toString() + " - " + data.getEndDate().toString()
        binding.period.text = date
    }
}