package com.example.runningapp.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

class RunningScheduleEntry {
    private lateinit var title: String
    private lateinit var startDate: LocalDate
    private lateinit var endDate: LocalDate
    private var description : String = ""
    private var monday: Boolean = false
    private var tuesday: Boolean = false
    private var wednesday: Boolean = false
    private var thursday: Boolean = false
    private var friday: Boolean = false
    private var saturday: Boolean = false
    private var sunday: Boolean = false

    fun getTitle(): String {
        return title
    }

    fun getStartDate(): LocalDate {
        return startDate
    }

    fun getEndDate(): LocalDate {
        return endDate
    }

    object StaticFunctions {
        @RequiresApi(Build.VERSION_CODES.O)
        @JvmStatic
        fun getDummyData(): RunningScheduleEntry {
            val dummy = RunningScheduleEntry()
            dummy.title = "Test"
            dummy.startDate = LocalDate.of(2021, 12, 12)
            dummy.endDate = LocalDate.of(2021, 12, 31)
            dummy.monday = true
            dummy.friday = true
            return dummy
        }
    }

}