package com.example.runningapp.fragments.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.runningapp.AppApplication
import com.example.runningapp.R
import com.example.runningapp.databinding.FragmentHistoryGraphBinding
import com.example.runningapp.util.LineChartUtil.StaticFunctions.configureLineChartDuration
import com.example.runningapp.util.LineChartUtil.StaticFunctions.lineWidth
import com.example.runningapp.viewmodels.HistoryViewModel
import com.example.runningapp.viewmodels.HistoryViewModelFactory
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class HistoryGraphFragment : Fragment() {

    private val historyViewModel: HistoryViewModel by activityViewModels {
        HistoryViewModelFactory((activity?.application as AppApplication).runHistoryRepository)
    }
    private var _binding: FragmentHistoryGraphBinding? = null

    private val binding get() = _binding!!

    private lateinit var chart: LineChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHistoryGraphBinding.inflate(inflater, container, false)
        val root: View = binding.root

        chart = binding.lineChart

        context?.let { configureLineChartDuration(chart, it) }

        historyViewModel.currentRunHistoryEntryMetaDataWithMeasurements.observe(viewLifecycleOwner) { currentRunHistoryEntry ->
            val timeList : MutableList<Float> = mutableListOf()
            val paceList : MutableList<Float> = mutableListOf()
            val altitudeList : MutableList<Float> = mutableListOf()
            currentRunHistoryEntry?.measurements?.forEach{
                if(it.paceValue != null && it.altitudeValue != null) {
                    timeList.add(it.timeValue)
                    paceList.add(it.paceValue!!)
                    altitudeList.add(it.altitudeValue!!)
                }
            }
            val paceTimeSeries: MutableList<Entry> = mutableListOf()
            val altitudeTimeSeries: MutableList<Entry> = mutableListOf()
            for (i in timeList.indices) {
                Entry(timeList[i], paceList[i]).let { paceTimeSeries.add(it) }
                Entry(timeList[i], altitudeList[i]).let {
                    altitudeTimeSeries.add(
                        it
                    )
                }
            }

            val pace = LineDataSet(
                paceTimeSeries,
                root.context.resources.getString(R.string.pace_label)
            )
            pace.lineWidth = lineWidth
            pace.setDrawCircles(false)

            val altitude = LineDataSet(
                altitudeTimeSeries,
                root.context.resources.getString(R.string.altitude_label)
            )
            altitude.color = R.color.purple_200
            altitude.setCircleColor(R.color.purple_200)
            altitude.lineWidth = lineWidth
            altitude.setDrawCircles(false)

            chart.data = LineData(
                pace,
                altitude
            )
            chart.data.setDrawValues(false)
            chart.animateX(500)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}