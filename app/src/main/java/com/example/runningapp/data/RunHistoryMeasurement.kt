package com.example.runningapp.data

import androidx.room.Entity
import androidx.room.ForeignKey
import java.time.LocalDateTime

@Entity(tableName = "run_history_measurement", primaryKeys = ["runDate", "timeValue"],
    foreignKeys = [ForeignKey(
    entity = RunHistoryEntryMetaData::class,
    parentColumns = arrayOf("date"),
    childColumns = arrayOf("runDate"),
    onDelete = ForeignKey.CASCADE
    )]
)
class RunHistoryMeasurement(val runDate: LocalDateTime, val timeValue: Float) { //time in nanoseconds
    var paceValue: Float? = null
    var altitudeValue: Float? = null
    var latitudeValue: Double? = null
    var longitudeValue: Double? = null
}