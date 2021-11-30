package com.example.runningapp.data

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

class RunHistoryEntry {
    private lateinit var date: LocalDateTime
    private lateinit var timeValues: List<Float> //in minutes
    private lateinit var paceValues: List<Float>
    private lateinit var altitudeValues: List<Float>
    private var kmRun: Float = 0.0f

    fun getDate(): LocalDateTime {
        return date
    }

    fun getTimeValues(): List<Float> {
        return timeValues
    }

    fun getPaceValues(): List<Float> {
        return paceValues
    }

    fun getAltitudeValues(): List<Float> {
        return altitudeValues
    }


    object StaticFunctions {
        @RequiresApi(Build.VERSION_CODES.O)
        @JvmStatic
        fun getDummyData(): List<RunHistoryEntry> {
            val dummy = RunHistoryEntry()
            dummy.date = LocalDateTime.of(2021, 12, 12, 12, 30, 51)
            dummy.timeValues = mutableListOf(5F,10F,15F,20F)
            dummy.paceValues = mutableListOf(5F,4.7F,5.1F,5.3F)
            dummy.altitudeValues = mutableListOf(0.5F,0.7F,0.4F,0.6F)
            dummy.kmRun = 4.2F

            val dummy2 = RunHistoryEntry()
            dummy2.date = LocalDateTime.of(2021, 12, 14, 15, 25, 30)
            dummy2.timeValues = mutableListOf(5F,10F,15F,20F)
            dummy2.paceValues = mutableListOf(8F,7.7F,6.1F,4.3F)
            dummy2.altitudeValues = mutableListOf(4F,2.7F,2.4F,1.6F)
            dummy2.kmRun = 3.2F
            return arrayListOf(dummy,dummy2)
        }
    }
}