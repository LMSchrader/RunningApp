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
import com.example.runningapp.adapters.HistoryAdapter
import com.example.runningapp.databinding.FragmentRecyclerViewBinding
import com.example.runningapp.viewmodels.HistoryViewModel
import com.example.runningapp.viewmodels.HistoryViewModelFactory

class HistoryRecyclerViewFragment : Fragment() {

    private val historyViewModel: HistoryViewModel by activityViewModels {
        HistoryViewModelFactory((activity?.application as AppApplication).runHistoryRepository)
    }
    private var _binding: FragmentRecyclerViewBinding? = null

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<HistoryAdapter.ViewHolder>? = null

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

        adapter = HistoryAdapter(historyViewModel.runHistoryEntries,
            { position ->
                historyViewModel.currentRunHistoryEntry.value = position?.let { it ->
                    historyViewModel.runHistoryEntries.value?.get(
                        it
                    )
                }
                if (!historyViewModel.historyFragmentIsInSplitScreenMode) {
                    (parentFragment as HistoryFragment).doForwardTransition()
                }
            }, viewLifecycleOwner
        )
        binding.recyclerView.adapter = adapter

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