package com.example.runningapp.fragments.dialogs

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment : DialogFragment() {
    private lateinit var listener: OnDateSetListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = parentFragment as OnDateSetListener
        } catch (e: ClassCastException) {
            throw ClassCastException((parentFragment.toString() + " must implement OnDateSetListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return context?.let { DatePickerDialog(it, listener, year, month, day) } as Dialog
    }

    companion object {
        const val TAG = "DatePickerFragment"
    }
}
