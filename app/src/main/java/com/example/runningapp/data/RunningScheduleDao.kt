package com.example.runningapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RunningScheduleDao {
    @Query("SELECT * FROM running_schedule ORDER by startDate")
    fun getAll(): Flow<List<RunningScheduleEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entry: RunningScheduleEntry)

    @Delete
    fun delete(entry: RunningScheduleEntry)

    @Update
    fun update(entry: RunningScheduleEntry)
}