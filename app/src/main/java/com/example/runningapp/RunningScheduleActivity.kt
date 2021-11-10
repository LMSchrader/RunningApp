package com.example.runningapp

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.runningapp.databinding.ActivityRunningScheduleBinding

class RunningScheduleActivity : Activity() {
    private lateinit var binding: ActivityRunningScheduleBinding
    private var layoutManager : RecyclerView.LayoutManager? = null
    private var adapter : RecyclerView.Adapter<RunningScheduleAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRunningScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        adapter = RunningScheduleAdapter()
        binding.recyclerView.adapter = adapter

        binding.fab.setOnClickListener {
            // TODO: aendern
            Toast.makeText(applicationContext, "replace with an action", Toast.LENGTH_LONG).show()
        }
    }
}