package com.example.runningapp.util

import android.app.DatePickerDialog
import android.content.Context
import java.time.LocalDate
import java.util.*

class DatePickerUtil {

    object StaticFunctions {
        private const val datePickerStyle: Int = android.R.style.Theme_DeviceDefault_Dialog

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
        fun initDatePicker(
            context: Context,
            dateSetListener: DatePickerDialog.OnDateSetListener
        ): DatePickerDialog {
            val cal: Calendar = Calendar.getInstance()
            val year: Int = cal.get(Calendar.YEAR)
            val month: Int = cal.get(Calendar.MONTH)
            val day: Int = cal.get(Calendar.DAY_OF_MONTH)

            return DatePickerDialog(
                context,
                datePickerStyle,
                dateSetListener,
                year,
                month,
                day
            )
        }
    }
}