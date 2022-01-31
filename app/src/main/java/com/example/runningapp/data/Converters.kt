package com.example.runningapp.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

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

    private val gson: Gson = Gson()

    @TypeConverter
    fun stringToFloatList(data: String?): List<Float?>? {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type = object : TypeToken<List<Float?>?>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun floatListToString(list: List<Float?>?): String? {
        return gson.toJson(list)
    }

    @TypeConverter
    fun doubleToFloatList(data: String?): List<Double?>? {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type = object : TypeToken<List<Double?>?>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun doubleListToString(list: List<Double?>?): String? {
        return gson.toJson(list)
    }
}