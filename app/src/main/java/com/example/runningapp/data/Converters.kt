package com.example.runningapp.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

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

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun stringToDateTime(dateString: String): LocalDateTime? {
        return LocalDateTime.parse(dateString)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun dateTimeToString(date: LocalDateTime?): String? {
        return date?.toString()
    }

    var gson: Gson = Gson()

    @TypeConverter
    fun stringToFloatList(data: String?): List<Float?>? {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type = object : TypeToken<List<Float?>?>() {}.getType()
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun floatListToString(someObjects: List<Float?>?): String? {
        return gson.toJson(someObjects)
    }
}