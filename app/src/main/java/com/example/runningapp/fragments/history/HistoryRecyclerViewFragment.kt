package com.example.runningapp.fragments.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.runningapp.AppApplication
import com.example.runningapp.adapters.HistoryRecyclerViewAdapter
import com.example.runningapp.data.RunHistoryEntryMetaDataWithMeasurements
import com.example.runningapp.databinding.FragmentRecyclerViewBinding
import com.example.runningapp.viewmodels.HistoryViewModel
import com.example.runningapp.viewmodels.HistoryViewModelFactory

class HistoryRecyclerViewFragment : Fragment() {

    private val historyViewModel: HistoryViewModel by activityViewModels {
        HistoryViewModelFactory((activity?.application as AppApplication).runHistoryRepository)
    }
    private var _binding: FragmentRecyclerViewBinding? = null

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder>? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecyclerViewBinding.inflate(inflater, container, false)
        val root: View = binding.root


        // add recycler view adapter
        layoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager

        adapter = HistoryRecyclerViewAdapter(historyViewModel.runHistoryEntriesMetaDataWithMeasurements,
            { position ->
                historyViewModel.runHistoryEntriesMetaDataWithMeasurements.observe(viewLifecycleOwner) { runHistory : List<RunHistoryEntryMetaDataWithMeasurements> ->
                    historyViewModel.currentRunHistoryEntryMetaDataWithMeasurements.value = position?.let { it ->
                        runHistory[it]
                    }
                }

                historyViewModel.currentRecyclerViewPosition = position

                if (!historyViewModel.historyFragmentIsInSplitScreenMode) {
                    (parentFragment as HistoryFragment).doForwardTransition()
                }
            }, viewLifecycleOwner
        )
        binding.recyclerView.adapter = adapter

        if(historyViewModel.currentRecyclerViewPosition != null) {
            historyViewModel.runHistoryEntriesMetaDataWithMeasurements.observe(viewLifecycleOwner) { runHistory : List<RunHistoryEntryMetaDataWithMeasurements> ->
                historyViewModel.currentRunHistoryEntryMetaDataWithMeasurements.value = historyViewModel.currentRecyclerViewPosition?.let { it ->
                    runHistory[it]
                }
            }
        }


        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}