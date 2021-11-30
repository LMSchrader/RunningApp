package com.example.runningapp.fragments.history

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.runningapp.adapters.HistoryAdapter
import com.example.runningapp.databinding.FragmentRecyclerViewBinding
import com.example.runningapp.viewmodels.HistoryViewModel

class HistoryRecyclerViewFragment : Fragment() {

    private val historyViewModel: HistoryViewModel by activityViewModels()
    private var _binding: FragmentRecyclerViewBinding? = null

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<HistoryAdapter.ViewHolder>? = null

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

        adapter = HistoryAdapter(historyViewModel.getRunHistoryEntries(),
            {
                position ->  historyViewModel.currentRunHistoryEntry.value = position
                if(!historyViewModel.isInSplitScreenMode) {
                    (parentFragment as HistoryFragment).doForwardTransition()
                }
            }
            , viewLifecycleOwner)
        binding.recyclerView.adapter = adapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}