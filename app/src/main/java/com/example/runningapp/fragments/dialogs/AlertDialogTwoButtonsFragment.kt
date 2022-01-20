package com.example.runningapp.fragments.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.runningapp.R

class AlertDialogTwoButtonsFragment : DialogFragment() {
    private lateinit var text: String

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("text", text)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (savedInstanceState != null) {
            text = savedInstanceState.getString("text").toString()
        }

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

            val args = Bundle()
            args.putString("text", text)
            dialog.onSaveInstanceState(args)

            return dialog
        }
    }
}