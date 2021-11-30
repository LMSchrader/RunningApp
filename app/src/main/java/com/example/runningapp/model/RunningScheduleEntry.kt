package com.example.runningapp.model

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.runningapp.R
import java.time.LocalDate

@Entity(tableName = "running_schedule")
data class RunningScheduleEntry(
    @PrimaryKey(autoGenerate = true) private val id: Int,
    var title: String,
    var startDate: LocalDate,
    var endDate: LocalDate
) {
    var description: String = ""
    var monday: Boolean = false
    var tuesday: Boolean = false
    var wednesday: Boolean = false
    var thursday: Boolean = false
    var friday: Boolean = false
    var saturday: Boolean = false
    var sunday: Boolean = false

    fun getWeekdayString(context: Context): String {
        val sb = StringBuilder()

        if (monday) {
            sb.append(context.getString(R.string.monday))
            sb.append(" ")
        }
        if (tuesday) {
            sb.append(context.getString(R.string.tuesday))
            sb.append(" ")
        }
        if (wednesday) {
            sb.append(context.getString(R.string.wednesday))
            sb.append(" ")
        }
        if (thursday) {
            sb.append(context.getString(R.string.thursday))
            sb.append(" ")
        }
        if (friday) {
            sb.append(context.getString(R.string.friday))
            sb.append(" ")
        }
        if (saturday) {
            sb.append(context.getString(R.string.saturday))
            sb.append(" ")
        }
        if (sunday) {
            sb.append(context.getString(R.string.sunday))
            sb.append(" ")
        }

        return sb.toString()
    }

    //object StaticFunctions {
    //    @RequiresApi(Build.VERSION_CODES.O)
    //    @JvmStatic
    //    fun getDummyData(): List<RunningScheduleEntry> {
    //        val dummy = RunningScheduleEntry()
    //        dummy.title = "Test"
    //        dummy.startDate = LocalDate.of(2021, 12, 12)
    //        dummy.endDate = LocalDate.of(2021, 12, 31)
    //        dummy.monday = true
    //        dummy.friday = true
//
    //        val dummy2 = RunningScheduleEntry()
    //        dummy2.title = "Test2"
    //        dummy2.startDate = LocalDate.of(2021, 11, 12)
    //        dummy2.endDate = LocalDate.of(2022, 1, 31)
    //        dummy2.tuesday = true
    //        dummy2.thursday = true
    //        dummy2.saturday = true
//
    //        return arrayListOf(dummy, dummy2)
    //    }
    //}
}