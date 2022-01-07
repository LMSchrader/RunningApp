package com.example.runningapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RunningScheduleDao {
    @Query("SELECT * FROM running_schedule ORDER by startDate")
    fun getAll(): Flow<List<RunningScheduleEntry>>

    //@Query("SELECT startDate FROM running_schedule LIMIT 1") // TODO query schreiben
    //fun getNextRunningDay(): Flow<LocalDate>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entry: RunningScheduleEntry)

    @Delete
    fun delete(entry: RunningScheduleEntry)

    @Update
    fun update(entry: RunningScheduleEntry)

    //@Query("SELECT * FROM running_schedule WHERE ")
    //fun getAllRelevantSchedulesToDetermineTheNextRunningDay()
//
    //@Query("SELECT first(relevantSchedules.monday OR relevantSchedules.tuesday OR relevantSchedules.wednesday OR relevantSchedules.thursday OR relevantSchedules.friday OR relevantSchedules.saturday OR relevantSchedules.sunday) FROM (SELECT monday, tuesday, wednesday, thursday, friday, saturday, sunday FROM running_schedule WHERE date('now','localtime') BETWEEN startDate AND endDate) AS relevantSchedules WHERE (relevantSchedules.monday AND :mon) OR (relevantSchedules.tuesday AND :tue) OR (relevantSchedules.wednesday AND :wed) OR (relevantSchedules.thursday AND :thu) OR (relevantSchedules.friday AND :fri) OR (relevantSchedules.saturday AND :sat) OR (relevantSchedules.sunday AND :sun) ")
    //fun isTodayARunningDay(mon: Boolean, tue: Boolean, wed: Boolean, thu: Boolean, fri: Boolean, sat: Boolean, sun: Boolean): Flow<Boolean>

    @Query("SELECT COUNT(*)>0 FROM (SELECT monday, tuesday, wednesday, thursday, friday, saturday, sunday FROM running_schedule WHERE date('now','localtime') BETWEEN startDate AND endDate) AS relevantSchedules WHERE case cast (strftime('%w', 'now') as integer) when 0 then relevantSchedules.sunday when 1 then relevantSchedules.monday when 2 then relevantSchedules.tuesday when 3 then relevantSchedules.wednesday when 4 then relevantSchedules.thursday when 5 then relevantSchedules.friday else relevantSchedules.saturday end ")
    fun isTodayARunningDay(): Flow<Boolean>
}