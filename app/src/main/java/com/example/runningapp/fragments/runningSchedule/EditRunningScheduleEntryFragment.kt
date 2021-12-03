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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditRunningScheduleEntryBinding.inflate(inflater, container, false)


        viewModel.currentEntry.observe(viewLifecycleOwner) { currentEntry ->
            if (currentEntry != null) {
                binding.editTitle.setText(currentEntry.title)

                binding.checkBoxMonday.isChecked = currentEntry.monday
                binding.checkBoxTuesday.isChecked = currentEntry.tuesday
                binding.checkBoxWednesday.isChecked = currentEntry.wednesday
                binding.checkBoxThursday.isChecked = currentEntry.thursday
                binding.checkBoxFriday.isChecked = currentEntry.friday
                binding.checkBoxSaturday.isChecked = currentEntry.saturday
                binding.checkBoxSunday.isChecked = currentEntry.sunday

                binding.editStartingDate.text = currentEntry.startDate.toString()
                binding.editEndDate.text = currentEntry.endDate.toString()

                binding.editDescription.setText(currentEntry.description)
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
        val startDate = LocalDate.parse(binding.editStartingDate.text)
        val endDate = LocalDate.parse(binding.editEndDate.text)

        val editedEntry =
            RunningScheduleEntry(binding.editTitle.text.toString(), startDate, endDate)

        viewModel.currentEntry.value?.getId()?.let { editedEntry.setId(it) }
        editedEntry.description = binding.editDescription.text.toString()
        editedEntry.monday = binding.checkBoxMonday.isChecked
        editedEntry.tuesday = binding.checkBoxTuesday.isChecked
        editedEntry.wednesday = binding.checkBoxWednesday.isChecked
        editedEntry.thursday = binding.checkBoxThursday.isChecked
        editedEntry.friday = binding.checkBoxFriday.isChecked
        editedEntry.saturday = binding.checkBoxSaturday.isChecked
        editedEntry.sunday = binding.checkBoxSunday.isChecked

        return when (item.itemId) {
            android.R.id.home -> {
                if (viewModel.currentEntry.value?.equals(editedEntry) == false) {
                    context?.let {
                        activity?.let { it1 ->
                            showDialog(
                                getString(R.string.data_loss), it,
                                it1
                            )
                        }
                    }
                } else {
                    activity?.onBackPressed()
                }
                true
            }

            R.id.imageSave -> {
                when {
                    !editedEntry.isTitleSet() -> {
                        view?.let {
                            Snackbar.make(it, R.string.incorrect_title, LENGTH_LONG)
                                .show()
                        }
                    }
                    !editedEntry.isStartAndEndDateCorrectlyDefined() -> {
                        view?.let {
                            Snackbar.make(it, R.string.incorrect_date, LENGTH_LONG)
                                .show()
                        }
                    }
                    else -> {
                        viewModel.update(editedEntry)
                        activity?.onBackPressed()
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}