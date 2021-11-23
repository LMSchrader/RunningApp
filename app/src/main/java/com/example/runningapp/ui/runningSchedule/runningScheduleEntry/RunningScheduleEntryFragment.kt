package com.example.runningapp.ui.runningSchedule.runningScheduleEntry

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.runningapp.R
import com.example.runningapp.databinding.FragmentRunningScheduleEntryBinding
import com.example.runningapp.ui.runningSchedule.RunningScheduleViewModel
import com.example.runningapp.util.OrientationUtil.StaticFunctions.isLandscapeMode

class RunningScheduleEntryFragment : Fragment() { //TODO: wechsel portrait auf landscape: es soll wieder runningSCheduleFragment mit diesem Fragment als child angezeigt werden
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
                binding.title.text = entries[currentEntry].getTitle()
                binding.weekdays.text = context?.let { entries[currentEntry].getWeekdayString(it) }
                binding.startDate.text = entries[currentEntry].getStartDate().toString()
                binding.endDate.text = entries[currentEntry].getEndDate().toString()
                binding.description.text = entries[currentEntry].getDescription()
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
        if (context?.let { isLandscapeMode(it) } == true) {
            return when (item.itemId) {
                R.id.imageEdit -> {
                    //TODO: entry mitgeben
                    //TODO: bug
                    view?.findNavController()
                        ?.navigate(R.id.action_nav_running_schedule_to_nav_edit_running_schedule_entry)
                    true
                }

                R.id.imageDelete -> {
                    //TODO: entry loeschen
                    true
                }

                else -> super.onOptionsItemSelected(item)
            }
        } else {
            return when (item.itemId) {
                R.id.home -> {
                    activity?.onBackPressed()
                    return true
                }

                R.id.imageEdit -> {
                    //TODO: entry mitgeben
                    view?.findNavController()
                        ?.navigate(R.id.action_nav_running_schedule_entry_to_nav_edit_running_schedule_entry)
                    true
                }

                R.id.imageDelete -> {
                    //TODO: entry loeschen
                    activity?.onBackPressed()
                    true
                }

                else -> super.onOptionsItemSelected(item)
            }
        }
    }
}