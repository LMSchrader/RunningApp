package com.example.runningapp.fragments.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.runningapp.R

class CancelContinueDialogFragment : DialogFragment() {
    private lateinit var text: String
    private lateinit var listener: CancelContinueDialogListener

    interface CancelContinueDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putString("text", text)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = parentFragment as CancelContinueDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException((parentFragment.toString() + " must implement ContinueDialogListener"))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            with(savedInstanceState) {
                text = getString("text") ?: ""
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setMessage(text)
            .setPositiveButton(getString(R.string.continue_text)) { _, _ ->
                listener.onDialogPositiveClick(
                    this
                )
            } //activity?.onBackPressed() }
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .create()
    }

    companion object {
        const val TAG = "CancelContinueDialogFragment"

        fun getInstance(text: String): DialogFragment {
            val dialog = CancelContinueDialogFragment()
            dialog.text = text

            return dialog
        }
    }
}