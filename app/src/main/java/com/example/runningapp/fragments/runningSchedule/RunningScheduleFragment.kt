package com.example.runningapp.fragments.runningSchedule

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.navigation.findNavController
import com.example.runningapp.R
import com.example.runningapp.databinding.FragmentRunningScheduleBinding
import com.example.runningapp.util.OrientationUtil.StaticFunctions.isLandscapeMode
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RunningScheduleFragment : Fragment() {
    private var _binding: FragmentRunningScheduleBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // add fragments
        if (savedInstanceState == null) {
            childFragmentManager.commit {
                setReorderingAllowed(true)
                add<RunningScheduleRecyclerViewFragment>(R.id.leftFragment)
            }
        } else if (context?.let { isLandscapeMode(it) } == true && parentFragmentManager.findFragmentById(
                R.id.rightFragment
            ) == null) {
            // add right fragment in landscape mode if not present
            addSecondFragment()
        } else if (context?.let { isLandscapeMode(it) } == false && childFragmentManager.findFragmentById(
                R.id.rightFragment
            ) != null) {
            // remove right fragment in portrait mode if present
            removeSecondFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRunningScheduleBinding.inflate(inflater, container, false)

        binding.addButton.setOnClickListener {
            view?.findNavController()
                ?.navigate(R.id.action_nav_running_schedule_to_nav_add_running_schedule_entry)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun addSecondFragment() {
        childFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RunningScheduleEntryFragment>(R.id.rightFragment)
        }
    }

    fun removeSecondFragment() {
        childFragmentManager.commit {
            setReorderingAllowed(true)
            remove(childFragmentManager.findFragmentById(R.id.rightFragment)!!)
        }
    }

    fun getAddButton(): FloatingActionButton {
        return binding.addButton
    }
}