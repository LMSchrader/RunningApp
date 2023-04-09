package com.example.runningapp.data

import androidx.room.Embedded
import androidx.room.Relation
import java.time.LocalDateTime
import kotlin.time.DurationUnit
import kotlin.time.toDuration

data class RunHistoryEntryMetaDataWithMeasurements(
    @Embedded
    val metaData: RunHistoryEntryMetaData,
    @Relation(
        parentColumn = "date",
        entityColumn = "runDate"
    )
    val measurements: MutableList<RunHistoryMeasurement>
) {
    fun getAveragePaceAsString(): String {
        if (measurements.isNotEmpty()) {
            var sum = 0F
            var count = 0
            measurements.forEach {
                if (it.paceValue != null) {
                    sum += it.paceValue!!
                    count++
                }
            }
            val average = sum / count
            return if (average.toLong().toDuration(DurationUnit.MINUTES).inWholeDays > 1) {
                ""
            } else {
                if (average.toLong().toDuration(DurationUnit.MINUTES).inWholeHours > 1) {
                    average.toLong().toDuration(DurationUnit.MINUTES)
                        .toString()
                } else {
                    average.times(60).toLong().toDuration(DurationUnit.SECONDS)
                        .toString()
                }
            }
        } else {
            return ""
        }
    }

    object StaticFunctions {
        @JvmStatic
        fun getDummyData(): List<RunHistoryEntryMetaDataWithMeasurements> {
            val dummyLocalDateTime = LocalDateTime.of(2021, 12, 12, 12, 30, 51)
            val dummyRunHistoryEntryMetaData = RunHistoryEntryMetaData(dummyLocalDateTime)
            dummyRunHistoryEntryMetaData.kmRun = 4.2F
            dummyRunHistoryEntryMetaData.timeRun = 4.2000000000F

            val dummyRunHistoryMeasurement1 = RunHistoryMeasurement(dummyLocalDateTime, 5F)
            dummyRunHistoryMeasurement1.paceValue = 5F
            dummyRunHistoryMeasurement1.altitudeValue = 0.5F
            dummyRunHistoryMeasurement1.longitudeValue = 17.94
            dummyRunHistoryMeasurement1.latitudeValue = 59.25

            val dummyRunHistoryMeasurement2 = RunHistoryMeasurement(dummyLocalDateTime, 10F)
            dummyRunHistoryMeasurement2.paceValue = 4.7F
            dummyRunHistoryMeasurement2.altitudeValue = 0.7F
            dummyRunHistoryMeasurement2.longitudeValue = 18.18
            dummyRunHistoryMeasurement2.latitudeValue = 59.37

            val dummyRunHistoryMeasurement3 = RunHistoryMeasurement(dummyLocalDateTime, 15F)
            dummyRunHistoryMeasurement3.paceValue = 5.1F
            dummyRunHistoryMeasurement3.altitudeValue = 0.4F

            val dummyRunHistoryMeasurement4 = RunHistoryMeasurement(dummyLocalDateTime, 20F)
            dummyRunHistoryMeasurement4.paceValue = 5.3F
            dummyRunHistoryMeasurement4.altitudeValue = 0.6F

            val dummyRunHistoryMeasurementList = mutableListOf(
                dummyRunHistoryMeasurement1,
                dummyRunHistoryMeasurement2,
                dummyRunHistoryMeasurement3,
                dummyRunHistoryMeasurement4
            )
            val dummy = RunHistoryEntryMetaDataWithMeasurements(
                dummyRunHistoryEntryMetaData,
                dummyRunHistoryMeasurementList
            )

            val dummy2LocalDateTime = LocalDateTime.of(2021, 12, 14, 15, 25, 30)
            val dummy2RunHistoryEntryMetaData = RunHistoryEntryMetaData(dummy2LocalDateTime)
            dummy2RunHistoryEntryMetaData.kmRun = 3.5F
            dummy2RunHistoryEntryMetaData.timeRun = 3.50000000F

            val dummy2RunHistoryMeasurement1 = RunHistoryMeasurement(dummy2LocalDateTime, 5F)
            dummy2RunHistoryMeasurement1.paceValue = 8F
            dummy2RunHistoryMeasurement1.altitudeValue = 4F
            dummy2RunHistoryMeasurement1.longitudeValue = 19.94
            dummy2RunHistoryMeasurement1.latitudeValue = 61.25

            val dummy2RunHistoryMeasurement2 = RunHistoryMeasurement(dummy2LocalDateTime, 10F)
            dummy2RunHistoryMeasurement2.paceValue = 7.7F
            dummy2RunHistoryMeasurement2.altitudeValue = 2.7F
            dummy2RunHistoryMeasurement2.longitudeValue = 20.18
            dummy2RunHistoryMeasurement2.latitudeValue = 61.37

            val dummy2RunHistoryMeasurement3 = RunHistoryMeasurement(dummy2LocalDateTime, 15F)
            dummy2RunHistoryMeasurement3.paceValue = 6.1F
            dummy2RunHistoryMeasurement3.altitudeValue = 2.4F

            val dummy2RunHistoryMeasurement4 = RunHistoryMeasurement(dummy2LocalDateTime, 20F)
            dummy2RunHistoryMeasurement4.paceValue = 4.3F
            dummy2RunHistoryMeasurement4.altitudeValue = 1.6F

            val dummy2RunHistoryMeasurementList = mutableListOf(
                dummy2RunHistoryMeasurement1,
                dummy2RunHistoryMeasurement2,
                dummy2RunHistoryMeasurement3,
                dummy2RunHistoryMeasurement4
            )
            val dummy2 = RunHistoryEntryMetaDataWithMeasurements(
                dummy2RunHistoryEntryMetaData,
                dummy2RunHistoryMeasurementList
            )

            return arrayListOf(dummy, dummy2)
        }
    }
}

