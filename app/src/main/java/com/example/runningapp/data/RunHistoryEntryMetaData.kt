package com.example.runningapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import kotlin.math.pow
import kotlin.math.round
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Entity(tableName = "run_history_meta_data")
class RunHistoryEntryMetaData(
    @PrimaryKey val date: LocalDateTime
) {
    var kmRun: Float = 0.0f
    var timeRun: Float = 0.0f

    fun getKmRunAsString(): String {
        return "%.2f".format(kmRun)
    }

    fun getTimeRunAsString(): String {
        return round(timeRun.toLong().div(10.toDouble().pow(9)))  //convert nanoseconds to seconds
            .toDuration(DurationUnit.SECONDS).toString()
    }
}