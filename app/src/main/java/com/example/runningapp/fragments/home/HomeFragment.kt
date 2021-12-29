package com.example.runningapp.fragments.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.runningapp.AppApplication
import com.example.runningapp.R
import com.example.runningapp.adapters.HomeAdapter
import com.example.runningapp.databinding.FragmentHomeBinding
import com.example.runningapp.viewmodels.RunningScheduleViewModel
import com.example.runningapp.viewmodels.RunningScheduleViewModelFactory

class HomeFragment : Fragment() {
    private val viewModel: RunningScheduleViewModel by activityViewModels {
        RunningScheduleViewModelFactory((activity?.application as AppApplication).runningScheduleRepository)
    }
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        viewModel.nextRunningDay.observe(viewLifecycleOwner) { runningDay ->
            if (runningDay != null) {
                binding.nextRunningDayDate.text = runningDay.toString()
            } else {
                binding.nextRunningDayDate.text = getString(R.string.to_short)
            }
        }

        initKeyValuePairs()

        binding.recordRunButton.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_nav_home_to_nav_record_run)
        }

        binding.pager.adapter = HomeAdapter(viewModel.entries, viewLifecycleOwner)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initKeyValuePairs() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return

        val defaultValue = 0
        val runningDaysKept = sharedPref.getInt(getString(R.string.running_days_kept), defaultValue)
        val kilometersRun = sharedPref.getInt(getString(R.string.kilometers_run), defaultValue)

        binding.runningDaysKept.text = resources.getString(R.string.running_days_kept, runningDaysKept.toString())
        binding.kilometersRun.text = resources.getString(R.string.kilometers_run, kilometersRun.toString())
        // write
        //with (sharedPref.edit()) {
        //    putInt(getString(R.string.running_days_kept), newHighScore)
        //    apply()
        //}

    }
}