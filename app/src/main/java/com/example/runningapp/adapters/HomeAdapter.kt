package com.example.runningapp.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    graphDataLiveModel: Map<String, LiveData<*>>,
    lifecycleOwner: LifecycleOwner,
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    private var paceData: MutableList<Float> = mutableListOf()
    private var kmData: MutableList<Float> = mutableListOf()
    private var timeData: MutableList<Float> = mutableListOf()
    private var paceTime: MutableList<Float> = mutableListOf()
    private var kmTime: MutableList<Float> = mutableListOf()
    private var timeTime: MutableList<Float> = mutableListOf()
    private var dataMap: Map<Int, MutableList<Float>> = mapOf(0 to paceData, 1 to kmData, 2 to timeData)
    private var timePointsMap: Map<Int, MutableList<Float>> = mapOf(0 to paceTime, 1 to kmTime, 2 to timeTime)
    private var labelResourceMap: Map<Int, Int> = mapOf(0 to R.string.pace_run_label, 1 to R.string.kilometers_run_label, 2 to R.string.time_run_label)

    init {

        graphDataLiveModel.forEach { mapEntry ->
            when (mapEntry.key) {
                "paceData" -> mapEntry.value.observe(lifecycleOwner) { entry ->
                    paceData.clear()
                    paceTime.clear()
                    val averagePaceRunTupleList = entry as List<*>
                    averagePaceRunTupleList.forEach {
                        val averagePaceRunTuple = it as RunHistoryDao.SummedRunHistoryMetaDataTuple
                        if (averagePaceRunTuple.date != null && averagePaceRunTuple.summedValue != null) {
                            paceData.add(averagePaceRunTuple.summedValue)
                            paceTime.add(averagePaceRunTuple.date.toLocalDate().toEpochDay().toFloat())
                        }
                    }
                    notifyDataSetChanged()
                }
                "kmData" -> mapEntry.value.observe(lifecycleOwner) { entry ->
                    kmData.clear()
                    kmTime.clear()
                    val kmRunTupleList = entry as List<*>
                    kmRunTupleList.forEach {
                        val kmRunTuple = it as RunHistoryDao.SummedRunHistoryMetaDataTuple
                        if (kmRunTuple.date != null && kmRunTuple.summedValue != null) {
                            kmData.add(kmRunTuple.summedValue)
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
                        val timeRunTuple = it as RunHistoryDao.SummedRunHistoryMetaDataTuple
                        if (timeRunTuple.date != null && timeRunTuple.summedValue != null) {
                            var timeValue = 0F
                            if(timeRunTuple.summedValue > 0F) {
                                timeValue = timeRunTuple.summedValue.times(10.0.pow(-9)).div(60).toFloat()
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
            LayoutInflater.from(viewGroup.context).inflate(R.layout.fragment_history_graph, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       LineChartUtil.StaticFunctions.configureLineChartDate(holder.chart, holder.context)

        holder.chart.description.isEnabled = true

        holder.chart.setDrawGridBackground(false)

        val xAxisPointsList = timePointsMap[position]
        val yAxisPointsList = dataMap[position]
        val timeSeries: MutableList<Entry> = mutableListOf()
        if (xAxisPointsList != null && yAxisPointsList !=null) {
            for (i in xAxisPointsList.indices) {
                timeSeries.add(Entry(xAxisPointsList[i], yAxisPointsList[i]))
            }
        }

        holder.chart.data = LineData(
            LineDataSet(
                timeSeries,
                labelResourceMap[position]?.let { holder.context.resources.getString(it) }
            )
        )
        holder.chart.animateX(500)
    }

    override fun getItemCount(): Int {
        return HomeAdapter.itemCount
    }

    class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        val context: Context = view.context
        val chart: LineChart = itemView.findViewById(R.id.lineChart)
    }

}