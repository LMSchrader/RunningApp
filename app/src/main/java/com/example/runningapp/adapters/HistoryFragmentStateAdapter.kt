package com.example.runningapp.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.runningapp.fragments.history.HistoryGraphFragment
import com.example.runningapp.fragments.history.HistoryMapFragment

class HistoryFragmentStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HistoryGraphFragment()
            1 -> HistoryMapFragment()
            else -> {
                HistoryGraphFragment()
            }
        }
    }
}