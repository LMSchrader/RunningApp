package com.example.runningapp.fragments.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.runningapp.R

class AlertDialogWithListenerFragment : DialogFragment() {
    private lateinit var text: String
    private lateinit var listener: NoticeDialogListener

    interface NoticeDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("text", text)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host fragment implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = parentFragment as NoticeDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException((parentFragment.toString() + " must implement NoticeDialogListener"))
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (savedInstanceState != null) {
            text = savedInstanceState.getString("text").toString()
        }

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
        const val TAG = "AlertDialogWithListenerFragment"

        fun getInstance(text: String): DialogFragment {
            val dialog = AlertDialogWithListenerFragment()
            dialog.text = text

            val args = Bundle()
            args.putString("text", text)
            dialog.onSaveInstanceState(args)

            return dialog
        }
    }
}
