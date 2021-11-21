package com.example.runningapp.ui.runningSchedule

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.runningapp.R
import com.example.runningapp.databinding.FragmentRecyclerViewBinding
import com.example.runningapp.ui.runningScheduleEntry.RunningScheduleEntryFragment
import com.example.runningapp.util.Util.StaticFunctions.isLandscapeMode

class RunningScheduleRecyclerViewFragment : Fragment() {
    private val viewModel: RunningScheduleViewModel by activityViewModels()
    private var _binding: FragmentRecyclerViewBinding? = null

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RunningScheduleAdapter.ViewHolder>? = null

    private val binding get() = _binding!!


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecyclerViewBinding.inflate(inflater, container, false)
        val root: View = binding.root

        layoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager

        adapter = RunningScheduleAdapter(viewModel.getEntries(), { position ->
            viewModel.currentEntry.value = position
            if (!isLandscapeMode()) {
                parentFragment?.childFragmentManager?.commit {
                    setReorderingAllowed(true)
                    replace<RunningScheduleEntryFragment>(R.id.recyclerView)
                    addToBackStack(null)
                }
            }
        }, viewLifecycleOwner)
        binding.recyclerView.adapter = adapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}