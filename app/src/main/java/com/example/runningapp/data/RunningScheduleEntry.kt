package com.example.runningapp.data

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.runningapp.R
import java.time.LocalDate

@Entity(tableName = "running_schedule")
data class RunningScheduleEntry(
    var title: String,
    var startDate: LocalDate,
    var endDate: LocalDate
) {
    @PrimaryKey(autoGenerate = true)
    private var id: Int? = null
    var description: String = ""
    var monday: Boolean = false
    var tuesday: Boolean = false
    var wednesday: Boolean = false
    var thursday: Boolean = false
    var friday: Boolean = false
    var saturday: Boolean = false
    var sunday: Boolean = false

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }

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

    fun isTitleSet(): Boolean {
        return title != ""
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isStartAndEndDateCorrectlyDefined(): Boolean {
        return startDate.isBefore(endDate) || startDate == endDate
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun equals(other: Any?): Boolean {
        return other is RunningScheduleEntry
                && this.id == other.id
                && this.title == other.title
                && this.startDate == other.startDate
                && this.endDate == other.endDate
                && this.description == other.description
                && this.monday == other.monday
                && this.tuesday == other.tuesday
                && this.wednesday == other.wednesday
                && this.thursday == other.thursday
                && this.friday == other.friday
                && this.saturday == other.saturday
                && this.sunday == other.sunday
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + startDate.hashCode()
        result = 31 * result + endDate.hashCode()
        result = 31 * result + (id ?: 0)
        result = 31 * result + description.hashCode()
        result = 31 * result + monday.hashCode()
        result = 31 * result + tuesday.hashCode()
        result = 31 * result + wednesday.hashCode()
        result = 31 * result + thursday.hashCode()
        result = 31 * result + friday.hashCode()
        result = 31 * result + saturday.hashCode()
        result = 31 * result + sunday.hashCode()
        return result
    }
}