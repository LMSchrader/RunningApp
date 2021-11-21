package com.example.runningapp.ui.runningSchedule

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.runningapp.databinding.FragmentRunningScheduleEntryBinding

class RunningScheduleEntryFragment : Fragment() {
    private val viewModel: RunningScheduleViewModel by activityViewModels()
    private var _binding: FragmentRunningScheduleEntryBinding? = null

    private val binding get() = _binding!!


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRunningScheduleEntryBinding.inflate(inflater, container, false)
        val root: View = binding.root


        viewModel.currentEntry.observe(viewLifecycleOwner) { currentEntry ->
            viewModel.getEntries().removeObservers(viewLifecycleOwner)
            viewModel.getEntries().observe(viewLifecycleOwner) { entries ->
                //val timeList = entries[currentEntry].getTimeValues()
                //val paceList = entries[currentEntry].getPaceValues()
                //val altitudeList = entries[currentEntry].getAltitudeValues()
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}