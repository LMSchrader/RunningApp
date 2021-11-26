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
    //TODO Navigation: running schedule entry ist im portrait modus und es wird die Orientierung zweimal gewechselt -> es wird die recycler view statt des entries angezeigt
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
            }
        } else if (context?.let { isLandscapeMode(it) } == true && parentFragmentManager.findFragmentById(
                R.id.rightFragment
            ) == null) {
            // add right fragment in landscape mode if not present
            //TODO: bedingung (nur wenn entry schonmal angesehen wurde) hinzufuegen
            addSecondFragment()
        } else if (context?.let { isLandscapeMode(it) } == false && childFragmentManager.findFragmentById(
                R.id.rightFragment
            ) != null) {
            // remove right fragment in portrait mode if present
            childFragmentManager.commit {
                setReorderingAllowed(true)
                remove(childFragmentManager.findFragmentById(R.id.rightFragment)!!)
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