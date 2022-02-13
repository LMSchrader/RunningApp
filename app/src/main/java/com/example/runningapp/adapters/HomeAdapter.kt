package com.example.runningapp.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.example.runningapp.R
import com.example.runningapp.data.RunHistoryDao
import com.example.runningapp.util.LineChartUtil
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlin.math.pow

class HomeAdapter(
    graphLiveDataMap: Map<String, LiveData<*>>,
    lifecycleOwner: LifecycleOwner,
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    private var paceData: MutableList<Float> = mutableListOf()
    private var kmData: MutableList<Float> = mutableListOf()
    private var timeData: MutableList<Float> = mutableListOf()
    private var paceTime: MutableList<Float> = mutableListOf()
    private var kmTime: MutableList<Float> = mutableListOf()
    private var timeTime: MutableList<Float> = mutableListOf()
    private var dataMap: Map<Int, MutableList<Float>> =
        mapOf(0 to paceData, 1 to kmData, 2 to timeData)
    private var timePointsMap: Map<Int, MutableList<Float>> =
        mapOf(0 to paceTime, 1 to kmTime, 2 to timeTime)
    private var titleMap: Map<Int, Int> = mapOf(
        0 to R.string.pace_run_label,
        1 to R.string.kilometers_run_label,
        2 to R.string.time_run_label
    )
    private val labelMap: Map<Int, Int> =
        mapOf(0 to R.string.pace_label, 1 to R.string.kilometer_label, 2 to R.string.time_label)

    init {
//TODO: auslagern?
        graphLiveDataMap.forEach { mapEntry ->
            when (mapEntry.key) {
                "paceData" -> mapEntry.value.observe(lifecycleOwner) { entry ->
                    paceData.clear()
                    paceTime.clear()
                    val averagePaceRunTupleList = entry as List<*>
                    averagePaceRunTupleList.forEach {
                        val averagePaceRunTuple = it as RunHistoryDao.DailyMetaDataTuple
                        if (averagePaceRunTuple.date != null && averagePaceRunTuple.metaDataValue != null) {
                            paceData.add(averagePaceRunTuple.metaDataValue)
                            paceTime.add(
                                averagePaceRunTuple.date.toLocalDate().toEpochDay().toFloat()
                            )
                        }
                    }
                    notifyDataSetChanged()
                }
                "kmData" -> mapEntry.value.observe(lifecycleOwner) { entry ->
                    kmData.clear()
                    kmTime.clear()
                    val kmRunTupleList = entry as List<*>
                    kmRunTupleList.forEach {
                        val kmRunTuple = it as RunHistoryDao.DailyMetaDataTuple
                        if (kmRunTuple.date != null && kmRunTuple.metaDataValue != null) {
                            kmData.add(kmRunTuple.metaDataValue)
                            kmTime.add(kmRunTuple.date.toLocalDate().toEpochDay().toFloat())
                        }
                    }
                    notifyDataSetChanged()
                }
                "timeData" -> mapEntry.value.observe(lifecycleOwner) { entry ->
                    timeData.clear()
                    timeTime.clear()
                    val timeRunTupleList = entry as List<*>
                    timeRunTupleList.forEach {
                        val timeRunTuple = it as RunHistoryDao.DailyMetaDataTuple
                        if (timeRunTuple.date != null && timeRunTuple.metaDataValue != null) {
                            var timeValue = 0F
                            if (timeRunTuple.metaDataValue > 0F) {
                                timeValue =
                                    timeRunTuple.metaDataValue.times(10.0.pow(-9)).div(60).toFloat()
                            }
                            timeData.add(timeValue)
                            timeTime.add(timeRunTuple.date.toLocalDate().toEpochDay().toFloat())
                        }
                    }
                    notifyDataSetChanged()
                }
                else -> {
                }
            }
        }
    }

    companion object {
        private const val itemCount = 3
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.fragment_home_graph, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = titleMap[position]?.let { holder.context.resources.getString(it) }

        LineChartUtil.StaticFunctions.configureLineChartDate(
            holder.chart,
            holder.context,
            holder.context.getString(R.string.no_data_available)
        )

        holder.chart.axisRight.isEnabled = false
        holder.chart.xAxis.granularity = 1F

        val xAxisPointsList = timePointsMap[position]
        val yAxisPointsList = dataMap[position]
        val timeSeries: MutableList<Entry> = mutableListOf()
        if (xAxisPointsList != null && yAxisPointsList != null) {
            for (i in xAxisPointsList.indices) {
                timeSeries.add(Entry(xAxisPointsList[i], yAxisPointsList[i]))
            }
        }

        if (timeSeries.isNotEmpty()) {
            val data = LineDataSet(
                timeSeries,
                labelMap[position]?.let { holder.context.resources.getString(it) }
            )
            data.lineWidth = LineChartUtil.StaticFunctions.lineWidth
            data.circleRadius = 5f
            data.circleHoleColor = data.color

            holder.chart.data = LineData(
                data
            )
            holder.chart.data.setDrawValues(false)
        }

        holder.chart.animateX(500)
    }

    override fun getItemCount(): Int {
        return HomeAdapter.itemCount
    }

    class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        val context: Context = view.context
        val chart: LineChart = itemView.findViewById(R.id.lineChart)
        val title: TextView = itemView.findViewById(R.id.title)
    }

}