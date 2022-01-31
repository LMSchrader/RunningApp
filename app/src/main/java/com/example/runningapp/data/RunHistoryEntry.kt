package com.example.runningapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "run_history")
class RunHistoryEntry(
    @PrimaryKey val date: LocalDateTime
) {
    var timeValues: MutableList<Float> = mutableListOf() //in nanoseconds
    var paceValues: MutableList<Float?> = mutableListOf()
    var altitudeValues: MutableList<Float> = mutableListOf()
    var kmRun: Float = 0.0f
    var latitudeValues: MutableList<Double> = mutableListOf()
    var longitudeValues: MutableList<Double> = mutableListOf()


    object StaticFunctions {
        @JvmStatic
        fun getDummyData(): List<RunHistoryEntry> {
            val dummy = RunHistoryEntry(LocalDateTime.of(2021, 12, 12, 12, 30, 51))
            dummy.timeValues = mutableListOf(5F, 10F, 15F, 20F)
            dummy.paceValues = mutableListOf(5F, 4.7F, 5.1F, 5.3F)
            dummy.altitudeValues = mutableListOf(0.5F, 0.7F, 0.4F, 0.6F)
            dummy.kmRun = 4.2F
            dummy.longitudeValues =mutableListOf(17.94, 18.18)
            dummy.latitudeValues =mutableListOf(59.25, 59.37)

            val dummy2 = RunHistoryEntry(LocalDateTime.of(2021, 12, 14, 15, 25, 30))
            dummy2.timeValues = mutableListOf(5F, 10F, 15F, 20F)
            dummy2.paceValues = mutableListOf(8F, 7.7F, 6.1F, 4.3F)
            dummy2.altitudeValues = mutableListOf(4F, 2.7F, 2.4F, 1.6F)
            dummy2.kmRun = 3.2F
            dummy2.longitudeValues =mutableListOf(19.94, 20.18)
            dummy2.latitudeValues =mutableListOf(61.25, 61.37)
            return arrayListOf(dummy, dummy2)
        }
    }
}