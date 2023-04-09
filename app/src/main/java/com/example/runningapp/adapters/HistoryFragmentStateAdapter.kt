package com.example.runningapp.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.runningapp.fragments.history.HistoryGraphFragment
import com.example.runningapp.fragments.history.HistoryMapFragment
import com.example.runningapp.fragments.history.HistoryOverviewFragment

class HistoryFragmentStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HistoryOverviewFragment()
            1 -> HistoryGraphFragment()
            2 -> HistoryMapFragment()
            else -> {
                HistoryOverviewFragment()
            }
        }
    }
}