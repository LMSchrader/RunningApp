package com.example.runningapp.database

import androidx.room.*
import com.example.runningapp.model.RunningScheduleEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface RunningScheduleDao {
    @Query("SELECT * FROM running_schedule")
    fun getAll(): Flow<List<RunningScheduleEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entry: RunningScheduleEntry):Long

    @Delete
    fun delete(entry: RunningScheduleEntry)

    @Update
    fun update(entry: RunningScheduleEntry)
}