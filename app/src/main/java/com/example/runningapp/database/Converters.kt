package com.example.runningapp.database

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toDate(dateString: String): LocalDate? {
        return LocalDate.parse(dateString)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toDateString(date: LocalDate?): String? {
        return date?.toString()
    }
}