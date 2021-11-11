package com.example.runningapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.runningapp.databinding.ActivityRunningScheduleEntryBinding

class RunningScheduleEntryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRunningScheduleEntryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRunningScheduleEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.include.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false);

        binding.include.imageView3.setOnClickListener {
            finish()
        }
    }
}