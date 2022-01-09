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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entry: RunHistoryEntry): Long

    @Update
    fun update(entry: RunHistoryEntry)

    @Delete
    fun delete(entry: RunHistoryEntry)

    @Query("DELETE FROM run_history")
    fun deleteAll()
}
