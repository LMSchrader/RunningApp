package com.example.runningapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface RunHistoryDao {
    @Query("SELECT * FROM run_history ORDER by date")
    fun getAll(): Flow<List<RunHistoryEntry>>

    @Query("SELECT * FROM run_history Where date == :id")
    fun get(id: LocalDateTime): RunHistoryEntry

    @Query("SELECT * FROM run_history Where date == :id")
    fun getAsFlow(id: LocalDateTime): Flow<RunHistoryEntry>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entry: RunHistoryEntry): Long

    @Update
    fun update(entry: RunHistoryEntry)

    @Delete
    fun delete(entry: RunHistoryEntry)

    @Query("DELETE FROM run_history")
    fun deleteAll()

    //@Query("SELECT cast (strftime('%W', date) as integer) as Week, SUM(kmRun) FROM run_history WHERE date BETWEEN date('now','localtime','weekday 0','-27 day') AND date('now','localtime','weekday 0') GROUP BY cast (strftime('%W', date) as integer)")
    //fun getKilometersRunInTheLastFourWeeks(): Flow<List<List<Int>>>
}
