package com.example.runningapp.ui.runningSchedule.addRunningScheduleEntry

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.runningapp.R
import com.example.runningapp.databinding.FragmentEditRunningScheduleEntryBinding
import com.example.runningapp.ui.runningSchedule.RunningScheduleViewModel
import com.example.runningapp.util.DatePickerUtil.StaticFunctions.getTodaysDate
import com.example.runningapp.util.DatePickerUtil.StaticFunctions.initDatePicker
import java.time.LocalDate

class AddRunningScheduleEntryFragment : Fragment() {
    private lateinit var viewModel: RunningScheduleViewModel
    private var _binding: FragmentEditRunningScheduleEntryBinding? = null

    private lateinit var datePickerDialogFromDate: DatePickerDialog
    private lateinit var datePickerDialogToDate: DatePickerDialog
    private lateinit var startDate: LocalDate
    private lateinit var endDate: LocalDate

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this)[RunningScheduleViewModel::class.java]

        _binding = FragmentEditRunningScheduleEntryBinding.inflate(inflater, container, false)


        setHasOptionsMenu(true)

        // initialize date picker
        initDatePickerFromDate()
        initDatePickerToDate()
        binding.editStartingDate.text = getTodaysDate().toString()
        binding.editEndDate.text = getTodaysDate().toString()

        binding.editStartingDate.setOnClickListener {
            datePickerDialogFromDate.show()
        }

        binding.editEndDate.setOnClickListener {
            datePickerDialogToDate.show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_edit_running_schedule_entry, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun initDatePickerFromDate() {
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                startDate = LocalDate.of(year, month + 1, day)
                binding.editStartingDate.text = startDate.toString()
            }

        datePickerDialogFromDate = context?.let { initDatePicker(it, dateSetListener) }!!
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun initDatePickerToDate() {
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                endDate = LocalDate.of(year, month + 1, day)
                binding.editEndDate.text = endDate.toString()
            }

        datePickerDialogToDate = context?.let { initDatePicker(it, dateSetListener) }!!
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
                true
            }

            R.id.imageSave -> {
                //TODO: save entry
                activity?.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}