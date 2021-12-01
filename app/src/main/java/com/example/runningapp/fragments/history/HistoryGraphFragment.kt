package com.example.runningapp.fragments.history

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.runningapp.AppApplication
import com.example.runningapp.R

import com.example.runningapp.databinding.FragmentHistoryGraphBinding
import com.example.runningapp.viewmodels.HistoryViewModel
import com.example.runningapp.viewmodels.HistoryViewModelFactory
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class HistoryGraphFragment : Fragment() {

    private val historyViewModel: HistoryViewModel by activityViewModels{
        HistoryViewModelFactory((activity?.application as AppApplication).runHistoryRepository)
    }
    private var _binding: FragmentHistoryGraphBinding? = null

    private val binding get() = _binding!!

    private lateinit var chart: LineChart

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
                    val timeList = currentRunHistoryEntry?.timeValues
                    val paceList = currentRunHistoryEntry?.paceValues
                    val altitudeList = currentRunHistoryEntry?.altitudeValues
                    val paceTimeSeries: MutableList<Entry> = mutableListOf()
                    val altitudeTimeSeries: MutableList<Entry> = mutableListOf()
            if (timeList != null) {
                for (i in timeList.indices) {
                    paceList?.get(i)?.let { Entry(timeList[i], it) }?.let { paceTimeSeries.add(it) }
                    altitudeList?.get(i)?.let { Entry(timeList[i], it) }?.let {
                        altitudeTimeSeries.add(
                            it
                        )
                    }
                }
            }
                    chart.data = LineData(
                        LineDataSet(
                            paceTimeSeries,
                            root.context.resources.getString(R.string.pace_label)
                        ),
                        LineDataSet(
                            altitudeTimeSeries,
                            root.context.resources.getString(R.string.altitude_label)
                        )
                    )
                    chart.animateX(500)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}