package com.example.runningapp.util

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.formatter.ValueFormatter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.math.floor
import kotlin.math.pow
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class LineChartUtil {

    object StaticFunctions {
        const val lineWidth = 3f
        const val textSize = 11f

        /**
         * LineChart itself does not configure the colors in dark mode
         */
        @JvmStatic
        fun configureLineChartIfDarkMode(chart: LineChart, context: Context) {
            val xaxis: XAxis = chart.xAxis
            val yaxis: YAxis = chart.axisRight
            val yaxis2: YAxis = chart.axisLeft
            val legend = chart.legend

            if (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES) {
                xaxis.textColor = Color.WHITE
                yaxis.textColor = Color.WHITE
                yaxis2.textColor = Color.WHITE
                legend.textColor = Color.WHITE
            }
        }

        @JvmStatic
        fun configureLineChartDuration(chart: LineChart, context: Context, noDataAvailableText: String) {
            configureLineChartGeneral(chart, context, noDataAvailableText)

            chart.xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return floor(
                        value.toLong().div(10.toDouble().pow(9))
                    ).toDuration(DurationUnit.SECONDS).toString()
                }
            }
        }

        @JvmStatic
        fun configureLineChartDate(chart: LineChart, context: Context, noDataAvailableText: String) {
            chart.xAxis.spaceMax = 0.1f
            chart.xAxis.spaceMin = 0.1f

            configureLineChartGeneral(chart, context, noDataAvailableText)

            chart.xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return if(value-value.toInt() == 0.0F) {
                        val date = LocalDate.ofEpochDay(value.toLong())
                        val dateFormatter: DateTimeFormatter =
                            DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                        date.format(dateFormatter)
                    } else {
                        ""
                    }
                }
            }
        }

        @JvmStatic
        fun configureLineChartGeneral(chart: LineChart, context: Context, noDataAvailableText: String) {
            chart.description.isEnabled = false
            chart.setNoDataText(noDataAvailableText)

            chart.setDrawGridBackground(false)
            chart.xAxis.setDrawGridLines(true)
            chart.axisLeft.setDrawGridLines(true)
            chart.axisRight.setDrawGridLines(false)

            configureLineChartIfDarkMode(chart, context)

            chart.xAxis.textSize = textSize
            chart.axisLeft.textSize = textSize
            chart.axisRight.textSize = textSize
            chart.legend.textSize = textSize
        }

    }
}