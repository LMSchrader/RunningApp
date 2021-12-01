package com.example.runningapp.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "run_history")
class RunHistoryEntry(
    var date: LocalDateTime
) {
    @PrimaryKey(autoGenerate = true) private var id: Int? = null
    var timeValues: List<Float> = emptyList() //in minutes
    var paceValues: List<Float> = emptyList()
    var altitudeValues: List<Float> = emptyList()
    var kmRun: Float = 0.0f

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }


    object StaticFunctions {
        @RequiresApi(Build.VERSION_CODES.O)
        @JvmStatic
        fun getDummyData(): List<RunHistoryEntry> {
            val dummy = RunHistoryEntry(LocalDateTime.of(2021, 12, 12, 12, 30, 51))
            dummy.timeValues = mutableListOf(5F,10F,15F,20F)
            dummy.paceValues = mutableListOf(5F,4.7F,5.1F,5.3F)
            dummy.altitudeValues = mutableListOf(0.5F,0.7F,0.4F,0.6F)
            dummy.kmRun = 4.2F

            val dummy2 = RunHistoryEntry(LocalDateTime.of(2021, 12, 14, 15, 25, 30))
            dummy2.timeValues = mutableListOf(5F,10F,15F,20F)
            dummy2.paceValues = mutableListOf(8F,7.7F,6.1F,4.3F)
            dummy2.altitudeValues = mutableListOf(4F,2.7F,2.4F,1.6F)
            dummy2.kmRun = 3.2F
            return arrayListOf(dummy,dummy2)
        }
    }
}