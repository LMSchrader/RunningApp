package com.example.runningapp.ui.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.runningapp.databinding.FragmentRecordRunBinding

class RecordRunFragment : Fragment() {

    private lateinit var recordRunViewModel: RecordRunViewModel
    private var _binding: FragmentRecordRunBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        recordRunViewModel =
            ViewModelProvider(this)[RecordRunViewModel::class.java]

        _binding = FragmentRecordRunBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}