package com.example.runningapp.util

import android.app.DatePickerDialog
import android.content.Context
import java.time.LocalDate
import java.util.*

class DatePickerUtil {

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

        @JvmStatic
        fun initDatePicker( //TODO
            context: Context,
            dateSetListener: DatePickerDialog.OnDateSetListener
        ): DatePickerDialog {
            val cal: Calendar = Calendar.getInstance()
            val year: Int = cal.get(Calendar.YEAR)
            val month: Int = cal.get(Calendar.MONTH)
            val day: Int = cal.get(Calendar.DAY_OF_MONTH)

            return DatePickerDialog(
                context,
                dateSetListener,
                year,
                month,
                day
            )
        }
    }
}