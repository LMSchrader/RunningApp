package com.example.runningapp.fragments.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.runningapp.AppApplication
import com.example.runningapp.adapters.HistoryAdapter
import com.example.runningapp.data.RunHistoryEntry
import com.example.runningapp.databinding.FragmentRecyclerViewBinding
import com.example.runningapp.viewmodels.HistoryViewModel
import com.example.runningapp.viewmodels.HistoryViewModelFactory
import com.google.android.material.snackbar.Snackbar
import java.time.format.DateTimeFormatter

class HistoryRecyclerViewFragment : Fragment() {

    private val historyViewModel: HistoryViewModel by activityViewModels{
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
            {position ->  historyViewModel.currentRunHistoryEntry.value = position?.let { it ->
                historyViewModel.runHistoryEntries.value?.get(
                    it
                )
            }
                if(!historyViewModel.isInSplitScreenMode) {
                    (parentFragment as HistoryFragment).doForwardTransition()
                }
            }
            , viewLifecycleOwner)
        binding.recyclerView.adapter = adapter

        addOnSwipedCallback()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Delete recycler view items when swiping to the right
     */
    private fun addOnSwipedCallback() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem: RunHistoryEntry =
                    historyViewModel.runHistoryEntries.value?.get(viewHolder.adapterPosition) ?: return

                historyViewModel.delete(deletedItem)

                val datePattern = "yyyy-MM-dd' 'HH:mm:ss"
                val formatter = DateTimeFormatter.ofPattern(datePattern)
                val date = deletedItem.date.format(formatter).toString()

                view?.let {
                    Snackbar.make(it, date, Snackbar.LENGTH_LONG)
                        .setAction(
                            "Undo"
                        ) {
                            historyViewModel.insert(deletedItem)
                        }
                }?.show()
            }
        }).attachToRecyclerView(binding.recyclerView)
    }
}