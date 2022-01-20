package com.example.runningapp.fragments.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.runningapp.R

class AlertDialogFragment : DialogFragment() {
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
            .setPositiveButton(R.string.ok) { _, _ -> }
            .create()
    }

    companion object {
        const val TAG = "AlertDialogFragment"

        fun getInstance(text: String): DialogFragment {
            val dialog = AlertDialogFragment()
            dialog.text = text

            val args = Bundle()
            args.putString("text", text)
            dialog.onSaveInstanceState(args)

            return dialog
        }
    }
}
