package com.example.runningapp.fragments.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.runningapp.R

class AlertDialogTwoButtonsFragment : DialogFragment() {
    private lateinit var text: String

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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setMessage(text)
            .setPositiveButton(getString(R.string.continue_text)) { _, _ -> activity?.onBackPressed() }
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .create()
    }

    companion object {
        const val TAG = "AlertDialogTwoButtonsFragment"

        fun getInstance(text: String): DialogFragment {
            val dialog = AlertDialogTwoButtonsFragment()
            dialog.text = text

            return dialog
        }
    }
}