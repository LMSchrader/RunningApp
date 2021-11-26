package com.example.runningapp.ui.runningSchedule

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.navigation.findNavController
import com.example.runningapp.R
import com.example.runningapp.databinding.FragmentRunningScheduleBinding
import com.example.runningapp.ui.runningSchedule.runningScheduleEntry.RunningScheduleEntryFragment
import com.example.runningapp.util.OrientationUtil.StaticFunctions.isLandscapeMode

class RunningScheduleFragment : Fragment() {
    //TODO: bug, menu items vom Fragment running schedule entry werden beim wechsel auf portrait weiterhin angezeigt
    private var _binding: FragmentRunningScheduleBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // add fragments
        if (savedInstanceState == null) {
            childFragmentManager.commit {
                setReorderingAllowed(true)
                add<RunningScheduleRecyclerViewFragment>(R.id.leftFragment)

                //if (context?.let { isLandscapeMode(it) } == true) {
                //    add<RunningScheduleEntryFragment>(R.id.rightFragment)
                //}
            }
        } //else if (context?.let { isLandscapeMode(it) } == true && parentFragmentManager.findFragmentById(
        //      R.id.rightFragment
        //  ) == null) {
        //  childFragmentManager.commit {
        //      setReorderingAllowed(true)
        //      add<RunningScheduleEntryFragment>(R.id.rightFragment)
        //  }
        //}

        val rightFragment = childFragmentManager.findFragmentById(R.id.rightFragment)
        if (context?.let { isLandscapeMode(it) } == false && rightFragment != null) {
            childFragmentManager.commit {
                setReorderingAllowed(true)
                remove(rightFragment)
            }
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
            add<RunningScheduleEntryFragment>(R.id.rightFragment)
        }
    }
}