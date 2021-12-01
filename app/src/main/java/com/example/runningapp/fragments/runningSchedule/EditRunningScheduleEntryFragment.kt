package com.example.runningapp.fragments.runningSchedule

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.runningapp.R
import com.example.runningapp.AppApplication
import com.example.runningapp.data.RunningScheduleEntry
import com.example.runningapp.databinding.FragmentEditRunningScheduleEntryBinding
import com.example.runningapp.util.DatePickerUtil.StaticFunctions.initDatePicker
import com.example.runningapp.util.DialogUtil.StaticFunctions.showDialog
import com.example.runningapp.viewmodels.RunningScheduleViewModel
import com.example.runningapp.viewmodels.RunningScheduleViewModelFactory
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate
import java.time.format.DateTimeParseException

class EditRunningScheduleEntryFragment : Fragment() {
    private val viewModel: RunningScheduleViewModel by activityViewModels {
        RunningScheduleViewModelFactory((activity?.application as AppApplication).runningScheduleRepository)
    }
    private var _binding: FragmentEditRunningScheduleEntryBinding? = null

    private lateinit var datePickerDialogStartDate: DatePickerDialog
    private lateinit var datePickerDialogEndDate: DatePickerDialog

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var entry: RunningScheduleEntry

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditRunningScheduleEntryBinding.inflate(inflater, container, false)


        viewModel.currentEntry.observe(viewLifecycleOwner) { entry ->
            if (entry != null) {
                binding.editTitle.setText(entry.title)

                binding.checkBoxMonday.isChecked = entry.monday
                binding.checkBoxTuesday.isChecked = entry.tuesday
                binding.checkBoxWednesday.isChecked = entry.wednesday
                binding.checkBoxThursday.isChecked = entry.thursday
                binding.checkBoxFriday.isChecked = entry.friday
                binding.checkBoxSaturday.isChecked = entry.saturday
                binding.checkBoxSunday.isChecked = entry.sunday

                binding.editStartingDate.text = entry.startDate.toString()
                binding.editEndDate.text = entry.endDate.toString()

                binding.editDescription.setText(entry.description)
            }
        }

        setHasOptionsMenu(true)

        // initialize date picker
        initDatePickerStartDate()
        initDatePickerEndDate()

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

        datePickerDialogStartDate = context?.let {
            initDatePicker(
                it,
                dateSetListener
            )
        }!!
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun initDatePickerEndDate() {
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                val endDate = LocalDate.of(year, month + 1, day)
                binding.editEndDate.text = endDate.toString()
            }

        datePickerDialogEndDate = context?.let {
            initDatePicker(
                it,
                dateSetListener
            )
        }!!
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                //TODO nur wenn daten geändert wurden
                context?.let {
                    activity?.let { it1 ->
                        showDialog(
                            getString(R.string.data_loss), it,
                            it1
                        )
                    }
                }
                true
            }

            R.id.imageSave -> {
                val startDate: LocalDate
                val endDate: LocalDate
                try {
                    startDate = LocalDate.parse(binding.editStartingDate.text)
                    endDate = LocalDate.parse(binding.editEndDate.text)
                } catch (e: DateTimeParseException) {
                    //TOdo: Mitteilung, fehlerhafte Eingabe konkretisieren
                    view?.let { Snackbar.make(it, R.string.incorrect_input, LENGTH_LONG).show() }
                    return true
                }

                entry.title = binding.editTitle.text.toString()
                entry.startDate = startDate
                entry.endDate = endDate
                entry.description = binding.editDescription.text.toString()
                entry.monday = binding.checkBoxMonday.isChecked
                entry.tuesday = binding.checkBoxTuesday.isChecked
                entry.wednesday = binding.checkBoxWednesday.isChecked
                entry.thursday = binding.checkBoxThursday.isChecked
                entry.friday = binding.checkBoxFriday.isChecked
                entry.saturday = binding.checkBoxSaturday.isChecked
                entry.sunday = binding.checkBoxSunday.isChecked

                if (entry.isCorrectlyDefined()) {
                    viewModel.update(entry)

                    activity?.onBackPressed()
                } else {
                    view?.let { Snackbar.make(it, R.string.incorrect_input, LENGTH_LONG).show() }
                    //TODO: Mitteilung, fehlerhafte Eingabe konkretisieren
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}