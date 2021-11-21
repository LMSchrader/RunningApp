package com.example.runningapp.ui.runningScheduleEntry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.runningapp.databinding.FragmentRunningScheduleEntryBinding

class RunningScheduleEntryFragment : Fragment()  {
    private var _binding: FragmentRunningScheduleEntryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRunningScheduleEntryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //val textView: TextView = binding.textHome
        //runningScheduleViewModel.text.observe(viewLifecycleOwner, {
        //    textView.text = it
        //})

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}