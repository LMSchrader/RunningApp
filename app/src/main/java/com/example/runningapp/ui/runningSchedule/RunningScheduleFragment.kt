package com.example.runningapp.ui.runningSchedule

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.runningapp.EditRunningScheduleEntryActivity
import com.example.runningapp.R
import com.example.runningapp.databinding.FragmentRunningScheduleBinding
import com.example.runningapp.ui.runningScheduleEntry.RunningScheduleEntryFragment
import com.example.runningapp.util.Util.StaticFunctions.isLandscapeMode

class RunningScheduleFragment : Fragment() {

    private lateinit var runningScheduleViewModel: RunningScheduleViewModel
    private var _binding: FragmentRunningScheduleBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            childFragmentManager.commit {
                setReorderingAllowed(true)
                add<RunningScheduleRecyclerViewFragment>(R.id.recyclerView)

                if (isLandscapeMode()) {
                    add<RunningScheduleEntryFragment>(R.id.running_schedule_entry)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        runningScheduleViewModel =
            ViewModelProvider(this)[RunningScheduleViewModel::class.java]

        _binding = FragmentRunningScheduleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.addButton.setOnClickListener {
            val intent = Intent(activity, EditRunningScheduleEntryActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}