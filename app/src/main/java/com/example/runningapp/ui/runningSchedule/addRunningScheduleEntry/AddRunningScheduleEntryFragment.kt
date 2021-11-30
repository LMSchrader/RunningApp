package com.example.runningapp.ui.runningSchedule.addRunningScheduleEntry

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.runningapp.R
import com.example.runningapp.database.AppApplication
import com.example.runningapp.databinding.FragmentEditRunningScheduleEntryBinding
import com.example.runningapp.model.RunningScheduleEntry
import com.example.runningapp.ui.runningSchedule.RunningScheduleViewModel
import com.example.runningapp.ui.runningSchedule.RunningScheduleViewModelFactory
import com.example.runningapp.util.DatePickerUtil.StaticFunctions.getTodaysDate
import com.example.runningapp.util.DatePickerUtil.StaticFunctions.initDatePicker
import java.time.LocalDate

class AddRunningScheduleEntryFragment : Fragment() {
    private val viewModel: RunningScheduleViewModel by viewModels {
        RunningScheduleViewModelFactory((activity?.application as AppApplication).runningScheduleRepository)
    }
    private var _binding: FragmentEditRunningScheduleEntryBinding? = null

    private lateinit var datePickerDialogStartDate: DatePickerDialog
    private lateinit var datePickerDialogEndDate: DatePickerDialog

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditRunningScheduleEntryBinding.inflate(inflater, container, false)


        setHasOptionsMenu(true)

        // initialize date picker
        initDatePickerStartDate()
        initDatePickerEndDate()
        binding.editStartingDate.text = getTodaysDate().toString()
        binding.editEndDate.text = getTodaysDate().toString()

        binding.editStartingDate.setOnClickListener {
            datePickerDialogStartDate.show()
        }

        binding.editEndDate.setOnClickListener {
            datePickerDialogEndDate.show()
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
    fun initDatePickerStartDate() {
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                val startDate = LocalDate.of(year, month + 1, day)
                binding.editStartingDate.text = startDate.toString()
            }

        datePickerDialogStartDate = context?.let { initDatePicker(it, dateSetListener) }!!
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun initDatePickerEndDate() {
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                val endDate = LocalDate.of(year, month + 1, day)
                binding.editEndDate.text = endDate.toString()
            }

        datePickerDialogEndDate = context?.let { initDatePicker(it, dateSetListener) }!!
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
                true
            }

            R.id.imageSave -> {
                val title = binding.editTitle.text.toString()
                val startDate = LocalDate.parse(binding.editStartingDate.text)
                val endDate = LocalDate.parse(binding.editEndDate.text)

                if (title != "" && startDate != null && !startDate.equals("") && endDate != null && !endDate.equals("")) {
                    val entry =
                        RunningScheduleEntry(0, title, startDate, endDate)
                    entry.description = binding.editDescription.text.toString()
                    entry.monday = binding.checkBoxMonday.isChecked
                    entry.tuesday = binding.checkBoxTuesday.isChecked
                    entry.wednesday = binding.checkBoxWednesday.isChecked
                    entry.thursday = binding.checkBoxThursday.isChecked
                    entry.friday = binding.checkBoxFriday.isChecked
                    entry.saturday = binding.checkBoxSaturday.isChecked
                    entry.sunday = binding.checkBoxSunday.isChecked
                    viewModel.insert(entry)
                } else {
                    //TODO: Mitteilung
                }
                activity?.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}