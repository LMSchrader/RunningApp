package com.example.runningapp.fragments.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.runningapp.R

/**
 * Dialog with continue button
 */
class ContinueDialogFragment : DialogFragment() {
    private lateinit var text: String
    private lateinit var listener: ContinueDialogListener

    interface ContinueDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putString("text", text)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            with(savedInstanceState) {
                text = getString("text") ?: ""
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = parentFragment as ContinueDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException((parentFragment.toString() + " must implement ContinueDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setMessage(text)
            .setPositiveButton(
                R.string.continue_text
            ) { _, _ ->
                listener.onDialogPositiveClick(this)
            }
            .create()
    }

    companion object {
        const val TAG = "ContinueDialogFragment"

        fun getInstance(text: String): DialogFragment {
            val dialog = ContinueDialogFragment()
            dialog.text = text

            return dialog
        }
    }
}
