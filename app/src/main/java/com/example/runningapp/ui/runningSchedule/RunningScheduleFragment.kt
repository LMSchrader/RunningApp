package com.example.runningapp.ui.runningSchedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.runningapp.R
import com.example.runningapp.databinding.FragmentRunningScheduleBinding
import com.example.runningapp.util.Util.StaticFunctions.isLandscapeMode

class RunningScheduleFragment : Fragment() {

    private lateinit var viewModel: RunningScheduleViewModel
    private var _binding: FragmentRunningScheduleBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            childFragmentManager.commit {
                setReorderingAllowed(true)
                add<RunningScheduleRecyclerViewFragment>(R.id.leftFragment)

                if (context?.let { isLandscapeMode(it) } == true) {
                    add<RunningScheduleEntryFragment>(R.id.rightFragment)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this)[RunningScheduleViewModel::class.java]

        _binding = FragmentRunningScheduleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.addButton.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_nav_running_schedule_to_nav_add_running_schedule_entry)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}