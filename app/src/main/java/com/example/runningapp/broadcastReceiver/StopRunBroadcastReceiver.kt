package com.example.runningapp.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.runningapp.services.RecordRunService

class StopRunBroadcastReceiver: BroadcastReceiver()  {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.stopService(Intent(context, RecordRunService::class.java))
    }
}