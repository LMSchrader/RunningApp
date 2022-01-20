package com.example.runningapp.util

import java.time.LocalDate
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
    }
}