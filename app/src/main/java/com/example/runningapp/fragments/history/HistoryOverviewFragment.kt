package com.example.runningapp.fragments.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.runningapp.AppApplication
import com.example.runningapp.databinding.FragmentHistoryOverviewBinding
import com.example.runningapp.util.DateUtil.StaticFunctions.formatDate
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

        historyViewModel.currentRunHistoryEntry.observe(viewLifecycleOwner) { currentRunHistoryEntry ->
            if (currentRunHistoryEntry != null) {
                binding.runFrom.text = formatDate(currentRunHistoryEntry.date)
                binding.distance.text = null // TODO
                binding.duration.text = null //TODO
                binding.pace.text = null //TODO
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