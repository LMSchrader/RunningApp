package com.example.runningapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.runningapp.databinding.ActivityRunningScheduleEntryBinding
import android.view.Menu
import android.view.MenuItem

class RunningScheduleEntryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRunningScheduleEntryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRunningScheduleEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.include.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.include.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_running_schedule_entry, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}