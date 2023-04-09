package com.example.runningapp.data

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime

class Converters {
    @TypeConverter
    fun toDate(dateString: String): LocalDate? {
        return LocalDate.parse(dateString)
    }

    @TypeConverter
    fun toDateString(date: LocalDate?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun stringToDateTime(dateString: String): LocalDateTime? {
        return LocalDateTime.parse(dateString)
    }

    @TypeConverter
    fun dateTimeToString(date: LocalDateTime?): String? {
        return date?.toString()
    }
}