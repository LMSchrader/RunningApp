package com.example.runningapp.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.runningapp.AppApplication
import com.example.runningapp.R
import com.example.runningapp.adapters.HomeAdapter
import com.example.runningapp.databinding.FragmentHomeBinding
import com.example.runningapp.viewmodels.RunningScheduleViewModel
import com.example.runningapp.viewmodels.RunningScheduleViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {
    private val viewModel: RunningScheduleViewModel by activityViewModels {
        RunningScheduleViewModelFactory((activity?.application as AppApplication).runningScheduleRepository)
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        viewModel.isTodayARunningDay.observe(viewLifecycleOwner) { runningDay ->
            if (runningDay) {
                binding.runningDay.text = getString(R.string.runningDay)
            } else {
                binding.runningDay.text = getString(R.string.restDay)
            }
        }

        initKeyValuePairs()

        binding.pager.adapter = HomeAdapter(viewModel.entries, viewLifecycleOwner)

        TabLayoutMediator(binding.tabLayout, binding.pager, true) { _, _ -> }.attach()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initKeyValuePairs() {
        val sharedPref = (activity?.application as AppApplication).shardPref

        val runningDaysKept =
            sharedPref.getInt(getString(R.string.running_days_kept_preferences), 0)
        val kilometersRun =
            "%.2f".format(sharedPref.getFloat(getString(R.string.kilometers_run_preferences), 0.0F))

        binding.runningDaysKept.text =
            getString(R.string.running_days_kept, runningDaysKept.toString())
        binding.kilometersRun.text = getString(R.string.kilometers_run, kilometersRun)
    }
}