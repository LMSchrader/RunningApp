package com.example.runningapp.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

import com.example.runningapp.databinding.FragmentHistoryGraphBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.DataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet


class HistoryGraphFragment : Fragment() {

    private lateinit var historyViewModel: HistoryViewModel
    private var _binding: FragmentHistoryGraphBinding? = null

    private val binding get() = _binding!!

    private lateinit var chart : LineChart

    fun generateData() : LineData {
        val dataList = mutableListOf(Entry(1F,1F), Entry(2F,2F), Entry(3F,3F), Entry(4F,4F))
        val dataSet = LineDataSet(dataList,"test")
        return LineData(dataSet)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        historyViewModel =
            ViewModelProvider(this)[HistoryViewModel::class.java]

        _binding = FragmentHistoryGraphBinding.inflate(inflater, container, false)
        val root: View = binding.root

        chart = binding.lineChart

        chart.description.isEnabled = true

        chart.setDrawGridBackground(false)

        chart.data = generateData()

        chart.animateX(3000)

        //val textView: TextView = binding.textHome
        //runningScheduleViewModel.text.observe(viewLifecycleOwner, {
        //    textView.text = it
        //})

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}