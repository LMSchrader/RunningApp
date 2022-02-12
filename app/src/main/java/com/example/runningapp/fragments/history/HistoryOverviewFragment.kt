package com.example.runningapp.fragments.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.runningapp.AppApplication
import com.example.runningapp.R
import com.example.runningapp.databinding.FragmentHistoryOverviewBinding
import com.example.runningapp.util.DateUtil.StaticFunctions.formatDate
import com.example.runningapp.viewmodels.HistoryViewModel
import com.example.runningapp.viewmodels.HistoryViewModelFactory
import kotlin.math.pow
import kotlin.math.round
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class HistoryOverviewFragment : Fragment() {
    private val historyViewModel: HistoryViewModel by activityViewModels {
        HistoryViewModelFactory((activity?.application as AppApplication).runHistoryRepository)
    }
    private var _binding: FragmentHistoryOverviewBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryOverviewBinding.inflate(inflater, container, false)
        val root: View = binding.root

        historyViewModel.currentRunHistoryEntryMetaDataWithMeasurements.observe(viewLifecycleOwner) { currentRunHistoryEntry ->
            if (currentRunHistoryEntry != null) {
                binding.runFrom.text = formatDate(currentRunHistoryEntry.metaData.date)
                binding.distance.text = "%.2f".format(currentRunHistoryEntry.metaData.kmRun)+" km"
                binding.duration.text = round(currentRunHistoryEntry.metaData.timeRun.toLong().div(10.toDouble().pow(9))).toDuration(DurationUnit.SECONDS).toString()
                if (currentRunHistoryEntry.measurements.isNotEmpty()) {
                    var sum = 0F
                    var count = 1
                    currentRunHistoryEntry.measurements.forEach{
                        if(it.paceValue != null) {
                            sum+=it.paceValue!!
                            count++
                        }
                    }
                    val average = sum/count
                    binding.pace.text = average.toLong().toDuration(DurationUnit.MINUTES).toString()
                } else {
                    binding.pace.text = getString(R.string.value_empty)
                }
            } else {
                //nothing to display // TODO
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}