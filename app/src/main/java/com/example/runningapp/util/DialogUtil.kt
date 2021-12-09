package com.example.runningapp.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.widget.TextView
import com.example.runningapp.R

class DialogUtil {

    object StaticFunctions {
        @JvmStatic
        fun showDialog(description: String, context: Context, activity: Activity) {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.alert_dialog)
            dialog.findViewById<TextView>(R.id.description)?.text = description

            val btnCancel: TextView? = dialog.findViewById(R.id.buttonCancel)
            btnCancel?.setOnClickListener {
                dialog.dismiss()
            }

            val btnContinue: TextView? = dialog.findViewById(R.id.buttonContinue)
            btnContinue?.setOnClickListener {
                dialog.dismiss()
                activity.onBackPressed()
            }
            dialog.show()
        }
    }
}