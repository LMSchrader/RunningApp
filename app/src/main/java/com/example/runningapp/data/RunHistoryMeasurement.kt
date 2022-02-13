package com.example.runningapp.data

import androidx.room.Entity
import androidx.room.ForeignKey
import java.time.LocalDateTime
import kotlin.math.pow
import kotlin.math.round
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Entity(
    tableName = "run_history_measurement", primaryKeys = ["runDate", "timeValue"],
    foreignKeys = [ForeignKey(
        entity = RunHistoryEntryMetaData::class,
        parentColumns = arrayOf("date"),
        childColumns = arrayOf("runDate"),
        onDelete = ForeignKey.CASCADE
    )]
)
class RunHistoryMeasurement(
    val runDate: LocalDateTime,
    val timeValue: Float
) { //time in nanoseconds
    var paceValue: Float? = null
    var altitudeValue: Float? = null
    var latitudeValue: Double? = null
    var longitudeValue: Double? = null

    fun getTimeValueAsString(): String {
        return round(timeValue.toLong().div(10.toDouble().pow(9)))
            .toDuration(DurationUnit.SECONDS).toString()
    }

    fun getPaceValueAsString(): String {
        return if (paceValue != null) {
            if (paceValue!!.toLong().toDuration(DurationUnit.MINUTES).inWholeDays > 1) {
                ""
            } else {
                if (paceValue!!.toLong().toDuration(DurationUnit.MINUTES).inWholeHours > 1) {
                    paceValue!!.toLong().toDuration(DurationUnit.MINUTES)
                        .toString()
                } else {
                    paceValue!!.times(60).toLong().toDuration(DurationUnit.SECONDS)
                        .toString()
                }
            }
        } else {
            ""
        }
    }
}