package com.example.runningapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "run_history_meta_data")
class RunHistoryEntryMetaData(
    @PrimaryKey val date: LocalDateTime
) {
    var kmRun: Float = 0.0f
    var timeRun: Float = 0.0f
}