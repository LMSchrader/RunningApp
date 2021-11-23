package com.example.runningapp.util

import android.content.Context
import android.content.res.Configuration

class OrientationUtil {

    object StaticFunctions {
        @JvmStatic
        fun isLandscapeMode(context: Context): Boolean {
            return context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        }
    }
}