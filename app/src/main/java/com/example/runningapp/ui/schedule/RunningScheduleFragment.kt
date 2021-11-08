package com.example.runningapp.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.runningapp.databinding.FragmentRunningScheduleBinding

class RunningScheduleFragment : Fragment() {

    private lateinit var runningScheduleViewModel: RunningScheduleViewModel
    private var _binding: FragmentRunningScheduleBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        runningScheduleViewModel =
            ViewModelProvider(this).get(RunningScheduleViewModel::class.java)

        _binding = FragmentRunningScheduleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        runningScheduleViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}