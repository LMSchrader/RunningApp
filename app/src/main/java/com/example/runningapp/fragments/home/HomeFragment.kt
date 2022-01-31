package com.example.runningapp.fragments.home

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
import com.google.android.material.tabs.TabLayoutMediator

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

        viewModel.isTodayARunningDay.observe(viewLifecycleOwner) { runningDay ->
            if (runningDay) {
                binding.runningDay.text = getString(R.string.runningDay)
            } else {
                binding.runningDay.text = getString(R.string.restDay)
            }
        }

        initKeyValuePairs()

        binding.recordRunButton.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_nav_home_to_nav_record_run)
        }

        binding.pager.adapter = HomeAdapter(viewModel.entries, viewLifecycleOwner)

        val tabLayoutMediator = TabLayoutMediator(binding.tabLayout, binding.pager, true)
        { tab, position -> }.attach()

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

        binding.runningDaysKept.text = getString(R.string.running_days_kept, runningDaysKept.toString())
        binding.kilometersRun.text = getString(R.string.kilometers_run, kilometersRun)
    }
}