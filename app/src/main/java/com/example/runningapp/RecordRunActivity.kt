package com.example.runningapp

import android.app.Activity
import android.os.Bundle
import com.example.runningapp.databinding.ActivityRecordRunBinding

class RecordRunActivity : Activity() {

    private lateinit var binding: ActivityRecordRunBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecordRunBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }

}