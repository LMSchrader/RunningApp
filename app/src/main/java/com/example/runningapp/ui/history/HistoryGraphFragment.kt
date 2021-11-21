package com.example.runningapp.ui.history

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.runningapp.R

import com.example.runningapp.databinding.FragmentHistoryGraphBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet


class HistoryGraphFragment : Fragment() {

    private val historyViewModel: HistoryViewModel by activityViewModels()
    private var _binding: FragmentHistoryGraphBinding? = null

    private val binding get() = _binding!!

    private lateinit var chart : LineChart

    fun generateData() : LineData {
        val dataList = mutableListOf(Entry(1F,1F), Entry(2F,2F), Entry(3F,3F), Entry(4F,4F))
        val dataSet = LineDataSet(dataList,"test")
        return LineData(dataSet)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHistoryGraphBinding.inflate(inflater, container, false)
        val root: View = binding.root

        chart = binding.lineChart

        chart.description.isEnabled = true

        chart.setDrawGridBackground(false)

        historyViewModel.currentRunHistoryEntry.observe(viewLifecycleOwner) { currentRunHistoryEntry ->
            val idx = currentRunHistoryEntry
            historyViewModel.getRunHistoryEntries().removeObservers(viewLifecycleOwner)
            historyViewModel.getRunHistoryEntries().observe(viewLifecycleOwner) { runHistoryEntries ->
                val timeList = runHistoryEntries[idx].getTimeValues()
                val paceList = runHistoryEntries[idx].getPaceValues()
                val altitudeList = runHistoryEntries[idx].getAltitudeValues()
                val paceTimeSeries : MutableList<Entry> = mutableListOf<Entry>()
                val altitudeTimeSeries : MutableList<Entry> = mutableListOf<Entry>()
                for (i in timeList.indices) {
                    paceTimeSeries.add(Entry(timeList[i],paceList[i]))
                    altitudeTimeSeries.add(Entry(timeList[i],altitudeList[i]))
                }
                chart.data = LineData(LineDataSet(paceTimeSeries, root.context.resources.getString(R.string.pace_label)),LineDataSet(altitudeTimeSeries, root.context.resources.getString(R.string.altitude_label)))
                chart.animateX(500)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}