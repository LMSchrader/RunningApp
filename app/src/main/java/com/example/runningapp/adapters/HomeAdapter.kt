package com.example.runningapp.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.example.runningapp.R
import com.example.runningapp.data.RunningScheduleEntry
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class HomeAdapter(
    runHistoryLiveData: LiveData<List<RunningScheduleEntry>>,
    lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    private var data: List<RunningScheduleEntry> = emptyList()

    init {
        runHistoryLiveData.observe(lifecycleOwner) { runningScheduleEntries ->
            data = runningScheduleEntries
            notifyDataSetChanged()
        }
    }

    companion object {
        private const val itemCount = 3
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.home_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //TODO: Daten aus Datenbank einbinden
        holder.chart.description.isEnabled = true

        holder.chart.setDrawGridBackground(false)

        val timeList = emptyList<Float>()
        val paceList = emptyList<Float>()
        val altitudeList = emptyList<Float>()
        val paceTimeSeries: MutableList<Entry> = mutableListOf()
        val altitudeTimeSeries: MutableList<Entry> = mutableListOf()
        if (timeList != null) {
            for (i in timeList.indices) {
                paceTimeSeries.add(Entry(timeList[i], paceList[i]))
                altitudeTimeSeries.add(Entry(timeList[i], altitudeList[i]))
            }
        }

        holder.chart.data = LineData(
            LineDataSet(
                paceTimeSeries,
                holder.context.resources.getString(R.string.pace_label)
            ),
            LineDataSet(
                altitudeTimeSeries,
                holder.context.resources.getString(R.string.altitude_label)
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