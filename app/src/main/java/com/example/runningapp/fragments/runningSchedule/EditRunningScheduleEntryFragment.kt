package com.example.runningapp.fragments.runningSchedule

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.runningapp.R
import com.example.runningapp.AppApplication
import com.example.runningapp.data.RunningScheduleEntry
import com.example.runningapp.databinding.FragmentEditRunningScheduleEntryBinding
import com.example.runningapp.fragments.dialogs.AlertDialogTwoButtonsFragment
import com.example.runningapp.fragments.dialogs.DatePickerFragment
import com.example.runningapp.util.KeyboardUtil
import com.example.runningapp.viewmodels.RunningScheduleViewModel
import com.example.runningapp.viewmodels.RunningScheduleViewModelFactory
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate

class EditRunningScheduleEntryFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    private val viewModel: RunningScheduleViewModel by activityViewModels {
        RunningScheduleViewModelFactory((activity?.application as AppApplication).runningScheduleRepository)
    }

    private var dialog: Int = 0

    private var _binding: FragmentEditRunningScheduleEntryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            with(savedInstanceState) {
                dialog = getInt("DatePickerFragment")
            }
        }
    }

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

        binding.editStartingDate.setOnClickListener {
            dialog = 1
            val datePickerFragment = DatePickerFragment()
            datePickerFragment.show(childFragmentManager, DatePickerFragment.TAG)
        }

        binding.editEndDate.setOnClickListener {
            dialog = 2
            val datePickerFragment = DatePickerFragment()
            datePickerFragment.show(childFragmentManager, DatePickerFragment.TAG)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        activity?.let { KeyboardUtil.StaticFunctions.hideKeyboard(it) }

        val editedEntry = buildRunningScheduleEntryObject()

        return when (item.itemId) {
            android.R.id.home -> {
                if (viewModel.currentEntry.value?.equals(editedEntry) == false) {
                    val dialog = AlertDialogTwoButtonsFragment.getInstance(getString(R.string.data_loss))
                    dialog.show(childFragmentManager, AlertDialogTwoButtonsFragment.TAG)
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

    private fun buildRunningScheduleEntryObject(): RunningScheduleEntry {
        val startDate = LocalDate.parse(binding.editStartingDate.text)
        val endDate = LocalDate.parse(binding.editEndDate.text)

        val editedEntry = RunningScheduleEntry(binding.editTitle.text.toString(), startDate, endDate)

        viewModel.currentEntry.value?.getId()?.let { editedEntry.setId(it) }
        editedEntry.description = binding.editDescription.text.toString()
        editedEntry.monday = binding.checkBoxMonday.isChecked
        editedEntry.tuesday = binding.checkBoxTuesday.isChecked
        editedEntry.wednesday = binding.checkBoxWednesday.isChecked
        editedEntry.thursday = binding.checkBoxThursday.isChecked
        editedEntry.friday = binding.checkBoxFriday.isChecked
        editedEntry.saturday = binding.checkBoxSaturday.isChecked
        editedEntry.sunday = binding.checkBoxSunday.isChecked

        return editedEntry
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = LocalDate.of(year, month + 1, dayOfMonth)

        if (dialog == 1) {
            binding.editStartingDate.text = date.toString()
        } else if (dialog == 2) {
            binding.editEndDate.text = date.toString()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putInt("DatePickerFragment", dialog)
        }
        super.onSaveInstanceState(outState)
    }
}