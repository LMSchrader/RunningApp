package com.example.runningapp.ui.runningSchedule

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.runningapp.R
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

        setHasOptionsMenu(true)

        viewModel.currentEntry.observe(viewLifecycleOwner) { currentEntry ->
            viewModel.getEntries().removeObservers(viewLifecycleOwner)
            viewModel.getEntries().observe(viewLifecycleOwner) { entries ->
                //val timeList = entries[currentEntry].getTimeValues()
                //val paceList = entries[currentEntry].getPaceValues()
                //val altitudeList = entries[currentEntry].getAltitudeValues()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_running_schedule_entry, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
                true
            }

            R.id.imageEdit -> {
                //TODO: entry mitgeben
                view?.findNavController()?.navigate(R.id.action_nav_running_schedule_entry_to_nav_edit_running_schedule_entry)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}