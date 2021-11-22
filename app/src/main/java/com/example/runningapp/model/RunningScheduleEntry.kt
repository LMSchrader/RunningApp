package com.example.runningapp.model

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.runningapp.R
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

    fun getDescription(): String {
        return description
    }

    fun getWeekdayString(context: Context): String {
        val sb = StringBuilder()

        if(monday) {
            sb.append(context.getString(R.string.monday))
            sb.append(" ")
        }
        if(tuesday) {
            sb.append(context.getString(R.string.tuesday))
            sb.append(" ")
        }
        if(wednesday) {
            sb.append(context.getString(R.string.wednesday))
            sb.append(" ")
        }
        if(thursday) {
            sb.append(context.getString(R.string.thursday))
            sb.append(" ")
        }
        if(friday) {
            sb.append(context.getString(R.string.friday))
            sb.append(" ")
        }
        if(saturday) {
            sb.append(context.getString(R.string.saturday))
            sb.append(" ")
        }
        if(sunday) {
            sb.append(context.getString(R.string.sunday))
            sb.append(" ")
        }

        return sb.toString()
    }

    object StaticFunctions {
        @RequiresApi(Build.VERSION_CODES.O)
        @JvmStatic
        fun getDummyData(): List<RunningScheduleEntry> {
            val dummy = RunningScheduleEntry()
            dummy.title = "Test"
            dummy.startDate = LocalDate.of(2021, 12, 12)
            dummy.endDate = LocalDate.of(2021, 12, 31)
            dummy.monday = true
            dummy.friday = true

            return arrayListOf(dummy, dummy)
        }
    }

}