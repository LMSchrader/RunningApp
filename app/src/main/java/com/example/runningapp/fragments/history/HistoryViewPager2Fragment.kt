package com.example.runningapp.fragments.history

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.runningapp.AppApplication
import com.example.runningapp.R
import com.example.runningapp.adapters.HistoryFragmentStateAdapter
import com.example.runningapp.databinding.FragmentHistoryViewPager2Binding
import com.example.runningapp.fragments.dialogs.CancelContinueDialogFragment
import com.example.runningapp.util.DateUtil.StaticFunctions.formatDate
import com.example.runningapp.viewmodels.HistoryViewModel
import com.example.runningapp.viewmodels.HistoryViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

class HistoryViewPager2Fragment : Fragment() {
    private val historyViewModel: HistoryViewModel by activityViewModels {
        HistoryViewModelFactory((activity?.application as AppApplication).runHistoryRepository)
    }

    private var _binding: FragmentHistoryViewPager2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHistoryViewPager2Binding.inflate(inflater, container, false)
        val root: View = binding.root

        setHasOptionsMenu(true)

        binding.pager.adapter = HistoryFragmentStateAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.pager, true) { _, _ -> }.attach()
        binding.pager.currentItem = historyViewModel.currentViewPagerItem

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_history_entry, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.imageDelete -> {
                showDeleteDialog()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        historyViewModel.currentViewPagerItem = binding.pager.currentItem
        _binding = null
    }

    private fun showDeleteDialog() {
        val deletedItem = historyViewModel.currentRunHistoryEntryMetaDataWithMeasurements.value ?: return
        val dateDeletedItem = formatDate(deletedItem.metaData.date)
        val dateActiveRun = (activity?.application as AppApplication).shardPref.getString(
            getString(R.string.service_active_preferences),
            ""
        )

        if (!dateActiveRun.equals(deletedItem.metaData.date.toString())) {
            // delete dialog
            val dialog =
                CancelContinueDialogFragment.getInstance(
                    getString(
                        R.string.delete_item,
                        dateDeletedItem
                    )
                )
            dialog.show(parentFragmentManager, CancelContinueDialogFragment.TAG)
        } else {
            // can't delete when run is still being recorded
            view?.let {
                Snackbar.make(
                    it,
                    getString(R.string.could_not_delete, dateDeletedItem),
                    Snackbar.LENGTH_LONG
                )
            }?.show()
        }
    }
}