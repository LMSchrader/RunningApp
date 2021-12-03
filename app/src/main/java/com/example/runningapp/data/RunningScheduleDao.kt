package com.example.runningapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface RunningScheduleDao {
    @Query("SELECT * FROM running_schedule ORDER by startDate")
    fun getAll(): Flow<List<RunningScheduleEntry>>

    @Query("SELECT startDate FROM running_schedule LIMIT 1") // TODO query schreiben
    fun getNextRunningDay(): Flow<LocalDate>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entry: RunningScheduleEntry)

    @Delete
    fun delete(entry: RunningScheduleEntry)

    @Update
    fun update(entry: RunningScheduleEntry)
}