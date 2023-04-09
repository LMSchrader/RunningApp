package com.example.runningapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface RunHistoryDao {

    @Transaction
    @Query("SELECT * FROM run_history_meta_data ORDER by date DESC")
    fun getAll(): Flow<List<RunHistoryEntryMetaDataWithMeasurements>>

    @Transaction
    @Query("SELECT * FROM run_history_meta_data Where date == :id")
    fun get(id: LocalDateTime): RunHistoryEntryMetaDataWithMeasurements

    @Transaction
    @Query("SELECT * FROM run_history_meta_data Where date == :id")
    fun getAsFlow(id: LocalDateTime): Flow<RunHistoryEntryMetaDataWithMeasurements>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entry: RunHistoryEntryMetaData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(measurement: List<RunHistoryMeasurement>)

    @Transaction
    fun insert(entryMetaDataWithMeasurements: RunHistoryEntryMetaDataWithMeasurements) {
        insert(entryMetaDataWithMeasurements.metaData)
        insertAll(entryMetaDataWithMeasurements.measurements)
    }

    @Update
    fun update(entry: RunHistoryEntryMetaData)

    @Update
    fun updateAll(measurement: List<RunHistoryMeasurement>)

    @Transaction
    fun update(entryMetaDataWithMeasurements: RunHistoryEntryMetaDataWithMeasurements) {
        update(entryMetaDataWithMeasurements.metaData)
        updateAll(entryMetaDataWithMeasurements.measurements)
    }

    @Transaction
    fun updateAndInsertLatestMeasurements(entryMetaDataWithMeasurements: RunHistoryEntryMetaDataWithMeasurements, startingIndexOfNewMeasurements : Int) {
        update(entryMetaDataWithMeasurements.metaData)
        val lastIndex = entryMetaDataWithMeasurements.measurements.lastIndex
        if(lastIndex>=startingIndexOfNewMeasurements && lastIndex>-1) {
            insertAll(
                entryMetaDataWithMeasurements.measurements.subList(
                    startingIndexOfNewMeasurements,
                    lastIndex+1
                )
            )
        }
    }

    @Delete
    fun delete(entry: RunHistoryEntryMetaData)

    @Delete
    fun deleteAll(measurement: List<RunHistoryMeasurement>)

    fun delete(entryMetaDataWithMeasurements: RunHistoryEntryMetaDataWithMeasurements) {
        delete(entryMetaDataWithMeasurements.metaData)
    }

    @Query("SELECT SUM(kmRun) as metaDataValue, date FROM run_history_meta_data WHERE date BETWEEN date('now','localtime', '-29 day', 'start of day') AND date('now','localtime', '+1 day', 'start of day') GROUP BY strftime('%Y %m %d', date) ORDER BY date")
    fun getKilometersRunForTheLastMonthPerDay() : Flow<List<DailyMetaDataTuple>>

    @Query("SELECT SUM(timeRun) as metaDataValue, date FROM run_history_meta_data WHERE date BETWEEN date('now','localtime', '-29 day', 'start of day') AND date('now','localtime', '+1 day', 'start of day') GROUP BY strftime('%Y %m %d', date) ORDER BY date")
    fun getTimeRunForTheLastMonthPerDay() : Flow<List<DailyMetaDataTuple>>

    @Query("SELECT AVG(paceValue) as metaDataValue, runDate as date FROM run_history_measurement WHERE runDate BETWEEN date('now','localtime', '-29 day', 'start of day') AND date('now','localtime', '+1 day', 'start of day') GROUP BY strftime('%Y %m %d', date) ORDER BY date")
    fun getAveragePaceRunForTheLastMonthPerDay() : Flow<List<DailyMetaDataTuple>>



    data class DailyMetaDataTuple(
        @ColumnInfo(name = "metaDataValue") val metaDataValue: Float?,
        @ColumnInfo(name = "date") val date: LocalDateTime?
    )
}
