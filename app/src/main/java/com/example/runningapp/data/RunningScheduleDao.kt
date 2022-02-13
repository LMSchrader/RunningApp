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

    //TODO: wann wird das geupdated
    @Query("SELECT COUNT(*)>0 FROM (SELECT monday, tuesday, wednesday, thursday, friday, saturday, sunday FROM running_schedule WHERE date('now','localtime') BETWEEN startDate AND endDate) AS relevantSchedules WHERE case cast (strftime('%w', 'now') as integer) when 0 then relevantSchedules.sunday when 1 then relevantSchedules.monday when 2 then relevantSchedules.tuesday when 3 then relevantSchedules.wednesday when 4 then relevantSchedules.thursday when 5 then relevantSchedules.friday else relevantSchedules.saturday end ")
    fun isTodayARunningDay(): Flow<Boolean>

    @Query("SELECT COUNT(*)>0 FROM (SELECT monday, tuesday, wednesday, thursday, friday, saturday, sunday FROM running_schedule WHERE date('now','localtime') BETWEEN startDate AND endDate) AS relevantSchedules WHERE case cast (strftime('%w', 'now') as integer) when 0 then relevantSchedules.sunday when 1 then relevantSchedules.monday when 2 then relevantSchedules.tuesday when 3 then relevantSchedules.wednesday when 4 then relevantSchedules.thursday when 5 then relevantSchedules.friday else relevantSchedules.saturday end ")
    fun isTodayARunningDayOneTimeRequest(): Boolean
}