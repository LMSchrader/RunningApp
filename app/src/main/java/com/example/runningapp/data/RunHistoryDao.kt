package com.example.runningapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RunHistoryDao {
    @Query("SELECT * FROM run_history ORDER by date")
    fun getAll(): Flow<List<RunHistoryEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entry: RunHistoryEntry)

    @Delete
    fun delete(entry: RunHistoryEntry)

    @Query("DELETE FROM run_history")
    fun deleteAll()
}
