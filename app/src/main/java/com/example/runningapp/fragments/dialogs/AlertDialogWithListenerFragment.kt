package com.example.runningapp.fragments.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.runningapp.R

class AlertDialogWithListenerFragment : DialogFragment() {
    private lateinit var text: String
    //private lateinit var listener: NoticeDialogListener
//
    //interface NoticeDialogListener {
    //    fun onDialogPositiveClick(dialog: DialogFragment)
    //}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (savedInstanceState != null) {
            text = savedInstanceState.getString("text").toString()
        }

        return AlertDialog.Builder(requireContext())
            .setMessage(text)
            .setPositiveButton(
                R.string.continue_text
            ) { _, _ ->
                //listener.onDialogPositiveClick(this)
                val bundle = Bundle()
                bundle.putString("result", getString(R.string.continue_text))
                parentFragmentManager.setFragmentResult("15557", bundle)
            }
            .create()
    }

    companion object {
        const val TAG = "AlertDialogWithListenerFragment"

        fun getInstance(text: String): DialogFragment  {
            val dialog = AlertDialogWithListenerFragment()
            dialog.text = text

            val args = Bundle()
            args.putString("text", text)
            dialog.onSaveInstanceState(args)

            return dialog
        }
    }
}
