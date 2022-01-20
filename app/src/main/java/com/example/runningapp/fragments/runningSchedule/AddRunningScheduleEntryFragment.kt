package com.example.runningapp.fragments.runningSchedule

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.runningapp.R
import com.example.runningapp.AppApplication
import com.example.runningapp.databinding.FragmentEditRunningScheduleEntryBinding
import com.example.runningapp.data.RunningScheduleEntry
import com.example.runningapp.fragments.dialogs.AlertDialogTwoButtonsFragment
import com.example.runningapp.fragments.dialogs.DatePickerFragment
import com.example.runningapp.util.DatePickerUtil.StaticFunctions.getTodaysDate
import com.example.runningapp.util.KeyboardUtil
import com.example.runningapp.viewmodels.RunningScheduleViewModel
import com.example.runningapp.viewmodels.RunningScheduleViewModelFactory
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate
import android.widget.DatePicker

class AddRunningScheduleEntryFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    private val viewModel: RunningScheduleViewModel by activityViewModels {
        RunningScheduleViewModelFactory((activity?.application as AppApplication).runningScheduleRepository)
    }
    private var _binding: FragmentEditRunningScheduleEntryBinding? = null

    //private lateinit var datePickerDialogStartDate: DatePickerDialog
    //private lateinit var datePickerDialogEndDate: DatePickerDialog

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditRunningScheduleEntryBinding.inflate(inflater, container, false)


        setHasOptionsMenu(true)

        binding.editStartingDate.text = getTodaysDate().toString()
        binding.editEndDate.text = getTodaysDate().toString()

        binding.editStartingDate.setOnClickListener {
            val newFragment = DatePickerFragment()
            activity?.let { it1 -> newFragment.show(it1.supportFragmentManager, DatePickerFragment.TAG) }

            //datePickerDialogStartDate.show()
        }

        binding.editEndDate.setOnClickListener {
            //datePickerDialogEndDate.show() //TODO
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun updateDate() {
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                val startDate = LocalDate.of(year, month + 1, day)
                binding.editStartingDate.text = startDate.toString()
            }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = LocalDate.of(year, month + 1, dayOfMonth)
        binding.editStartingDate.text = date.toString()
        //val c: Calendar = Calendar.getInstance()
        //c.set(Calendar.YEAR, year)
        //c.set(Calendar.MONTH, month)
        //c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        //val currentDate: String = DateFormat.getDateInstance().format(c.getTime())

        //dates = Integer.toString(dayOfMonth)
        //months = Integer.toString(month)
        //years = Integer.toString(year)
        //mDisplayDate.setText(currentDate)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_edit_running_schedule_entry, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //private fun initDatePickerStartDate() { //TODO
    //    val dateSetListener =
    //        DatePickerDialog.OnDateSetListener { _, year, month, day ->
    //            val startDate = LocalDate.of(year, month + 1, day)
    //            binding.editStartingDate.text = startDate.toString()
    //        }
//
    //    datePickerDialogStartDate = context?.let { initDatePicker(it, dateSetListener) }!!
    //}
//
    //private fun initDatePickerEndDate() { //TODO
    //    val dateSetListener =
    //        DatePickerDialog.OnDateSetListener { _, year, month, day ->
    //            val endDate = LocalDate.of(year, month + 1, day)
    //            binding.editEndDate.text = endDate.toString()
    //        }
//
    //    datePickerDialogEndDate = context?.let { initDatePicker(it, dateSetListener) }!!
    //}

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        activity?.let { KeyboardUtil.StaticFunctions.hideKeyboard(it) }

        val entry = buildRunningScheduleEntryObject()

        return when (item.itemId) {
            android.R.id.home -> {
                if (entry.notDefault()) {
                    val dialog = AlertDialogTwoButtonsFragment.getInstance(getString(R.string.data_loss))
                    dialog.show(childFragmentManager, AlertDialogTwoButtonsFragment.TAG)
                } else {
                    activity?.onBackPressed()
                }
                true
            }

            R.id.imageSave -> { //TODO snackbar überprüfen
                when {
                    !entry.isTitleSet() -> {
                        view?.let {
                            Snackbar.make(it, R.string.incorrect_title, LENGTH_LONG)
                                .show()
                        }
                    }
                    !entry.isStartAndEndDateCorrectlyDefined() -> {
                        view?.let {
                            Snackbar.make(it, R.string.incorrect_date, LENGTH_LONG)
                                .show()
                        }
                    }
                    else -> {
                        viewModel.insert(entry)
                        activity?.onBackPressed()
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun buildRunningScheduleEntryObject(): RunningScheduleEntry {
        val title = binding.editTitle.text.toString()
        val startDate = LocalDate.parse(binding.editStartingDate.text)
        val endDate = LocalDate.parse(binding.editEndDate.text)

        val entry = RunningScheduleEntry(title, startDate, endDate)

        entry.description = binding.editDescription.text.toString()

        entry.monday = binding.checkBoxMonday.isChecked
        entry.tuesday = binding.checkBoxTuesday.isChecked
        entry.wednesday = binding.checkBoxWednesday.isChecked
        entry.thursday = binding.checkBoxThursday.isChecked
        entry.friday = binding.checkBoxFriday.isChecked
        entry.saturday = binding.checkBoxSaturday.isChecked
        entry.sunday = binding.checkBoxSunday.isChecked

        return entry
    }
}