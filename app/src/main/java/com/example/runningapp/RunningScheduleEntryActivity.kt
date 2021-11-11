package com.example.runningapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.runningapp.databinding.ActivityRunningScheduleEntryBinding

class RunningScheduleEntryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRunningScheduleEntryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRunningScheduleEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.include.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.include.imageBack.setOnClickListener {
            finish()
        }

        binding.include.imageEdit.setOnClickListener {
            //TODO:implement
            Toast.makeText(applicationContext, "replace with an action", Toast.LENGTH_LONG).show()
        }

        binding.include.imageDelete.setOnClickListener {
            // TODO: delete entry
            finish()
        }
    }
}