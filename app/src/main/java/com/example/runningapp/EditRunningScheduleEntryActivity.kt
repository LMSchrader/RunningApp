package com.example.runningapp

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.runningapp.databinding.ActivityEditRunningScheduleEntryBinding
import java.time.LocalDate
import java.util.*

class EditRunningScheduleEntryActivity : AppCompatActivity() { //TODO: currently only add entry, add also edit functionality (same layout)
    private lateinit var binding: ActivityEditRunningScheduleEntryBinding
    private lateinit var datePickerDialogFromDate: DatePickerDialog
    private lateinit var datePickerDialogToDate: DatePickerDialog
    private lateinit var date: LocalDate

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditRunningScheduleEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.include.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.include.toolbar.setNavigationIcon(R.drawable.ic_baseline_clear_24)

        initDatePickerFromDate()
        initDatePickerToDate()
        binding.editStartingDate.text = getTodaysDate().toString()
        binding.editEndDate.text = getTodaysDate().toString()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_edit_running_schedule_entry, menu)
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTodaysDate(): LocalDate {
        val cal = Calendar.getInstance()
        return LocalDate.of(cal[Calendar.YEAR], cal[Calendar.MONTH]+1, cal[Calendar.DAY_OF_MONTH])
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun initDatePickerFromDate() {
        val dateSetListener =
            OnDateSetListener { _, year, month, day ->
                date = LocalDate.of(year, month+1, day)
                binding.editStartingDate.text = date.toString()
            }

        val cal: Calendar = Calendar.getInstance()
        val year: Int = cal.get(Calendar.YEAR)
        val month: Int = cal.get(Calendar.MONTH)
        val day: Int = cal.get(Calendar.DAY_OF_MONTH)

        datePickerDialogFromDate = DatePickerDialog(this, android.R.style.Theme_DeviceDefault_Dialog, dateSetListener, year, month, day)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun initDatePickerToDate() {
        val dateSetListener =
            OnDateSetListener { _, year, month, day ->
                date = LocalDate.of(year, month+1, day)
                binding.editEndDate.text = date.toString()
            }

        val cal: Calendar = Calendar.getInstance()
        val year: Int = cal.get(Calendar.YEAR)
        val month: Int = cal.get(Calendar.MONTH)
        val day: Int = cal.get(Calendar.DAY_OF_MONTH)

        datePickerDialogToDate = DatePickerDialog(this, android.R.style.Theme_DeviceDefault_Dialog, dateSetListener, year, month, day)
    }

    fun openDatePickerFromDate(view: View) {
        datePickerDialogFromDate.show()
    }

    fun openDatePickerToDate(view: View) {
        datePickerDialogToDate.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            R.id.imageSave -> {
                //TODO: save entry
                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}