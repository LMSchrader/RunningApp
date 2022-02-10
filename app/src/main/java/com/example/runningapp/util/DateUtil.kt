package com.example.runningapp.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DateUtil {

    object StaticFunctions {

        @JvmStatic
        fun getTodaysDate(): LocalDate {
            val cal = Calendar.getInstance()
            return LocalDate.of(
                cal[Calendar.YEAR],
                cal[Calendar.MONTH] + 1,
                cal[Calendar.DAY_OF_MONTH]
            )
        }

        fun formatDate(dateTime: LocalDateTime): String {
            val datePattern = "yyyy-MM-dd' 'HH:mm:ss"
            val formatter = DateTimeFormatter.ofPattern(datePattern)
            return dateTime.format(formatter).toString()
        }
    }
}