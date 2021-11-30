package com.example.runningapp.fragments.runningSchedule

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.runningapp.R
import com.example.runningapp.AppApplication
import com.example.runningapp.databinding.FragmentRunningScheduleEntryBinding
import com.example.runningapp.util.OrientationUtil.StaticFunctions.isLandscapeMode
import com.example.runningapp.viewmodels.RunningScheduleViewModel
import com.example.runningapp.viewmodels.RunningScheduleViewModelFactory

class RunningScheduleEntryFragment : Fragment() {
    private val viewModel: RunningScheduleViewModel by viewModels {
        RunningScheduleViewModelFactory((activity?.application as AppApplication).runningScheduleRepository)
    }
    private var _binding: FragmentRunningScheduleEntryBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // in landscape mode this fragment should not be displayed alone
        if (context?.let { isLandscapeMode(it) } == true && parentFragmentManager.findFragmentById(R.id.leftFragment) == null) {
            findNavController().popBackStack()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRunningScheduleEntryBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        viewModel.currentEntry.observe(viewLifecycleOwner) { currentEntry ->
            //viewModel.getEntries().removeObservers(viewLifecycleOwner)
            viewModel.entries.observe(viewLifecycleOwner) { entries ->
                binding.title.text = entries[currentEntry].title
                binding.weekdays.text = context?.let { entries[currentEntry].getWeekdayString(it) }
                binding.startDate.text = entries[currentEntry].startDate.toString()
                binding.endDate.text = entries[currentEntry].endDate.toString()
                binding.description.text = entries[currentEntry].description
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