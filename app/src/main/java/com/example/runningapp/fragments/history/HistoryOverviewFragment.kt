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
import com.example.runningapp.util.DateAndDateTimeUtil.StaticFunctions.formatLocalDateTime
import com.example.runningapp.viewmodels.HistoryViewModel
import com.example.runningapp.viewmodels.HistoryViewModelFactory

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
                binding.runFrom.text = formatLocalDateTime(currentRunHistoryEntry.metaData.date)
                binding.distance.text = getString(
                    R.string.kilometer_with_number,
                    currentRunHistoryEntry.metaData.getKmRunAsString()
                )
                binding.duration.text = currentRunHistoryEntry.metaData.getTimeRunAsString()

                if (currentRunHistoryEntry.getAveragePaceAsString().isEmpty()) {
                    binding.pace.text = getString(R.string.value_empty)
                } else {
                    binding.pace.text = currentRunHistoryEntry.getAveragePaceAsString()
                }
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}