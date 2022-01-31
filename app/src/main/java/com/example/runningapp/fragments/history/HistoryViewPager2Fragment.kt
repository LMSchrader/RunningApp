package com.example.runningapp.fragments.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.runningapp.adapters.HistoryFragmentStateAdapter
import com.example.runningapp.databinding.FragmentHistoryViewPager2Binding
import com.google.android.material.tabs.TabLayoutMediator

class HistoryViewPager2Fragment : Fragment() {
    private var _binding: FragmentHistoryViewPager2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHistoryViewPager2Binding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.pager.adapter = HistoryFragmentStateAdapter(this)

        val tabLayoutMediator = TabLayoutMediator(binding.tabLayout, binding.pager, true)
        { tab, position -> }.attach()

        return root
    }
}