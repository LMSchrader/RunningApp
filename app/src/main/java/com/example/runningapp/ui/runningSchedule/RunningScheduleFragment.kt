package com.example.runningapp.ui.runningSchedule

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.runningapp.EditRunningScheduleEntryActivity
import com.example.runningapp.RunningScheduleAdapter
import com.example.runningapp.databinding.FragmentRunningScheduleBinding

class RunningScheduleFragment : Fragment() {

    private lateinit var runningScheduleViewModel: RunningScheduleViewModel
    private var _binding: FragmentRunningScheduleBinding? = null

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RunningScheduleAdapter.ViewHolder>? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        runningScheduleViewModel =
            ViewModelProvider(this)[RunningScheduleViewModel::class.java]

        _binding = FragmentRunningScheduleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //val textView: TextView = binding.textHome
        //runningScheduleViewModel.text.observe(viewLifecycleOwner, {
        //    textView.text = it
        //})

        layoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager

        adapter = RunningScheduleAdapter()
        binding.recyclerView.adapter = adapter

        binding.addButton.setOnClickListener {
            val intent = Intent(activity, EditRunningScheduleEntryActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}