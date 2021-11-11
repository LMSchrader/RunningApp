package com.example.runningapp

import android.app.Activity
import android.os.Bundle
import com.example.runningapp.databinding.ActivityRunningScheduleEntryBinding

class RunningScheduleEntryActivity : Activity() {
    private lateinit var binding: ActivityRunningScheduleEntryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRunningScheduleEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}