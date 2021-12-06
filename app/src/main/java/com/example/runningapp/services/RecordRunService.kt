package com.example.runningapp.services

import android.app.Service
import android.content.Intent
import android.os.IBinder

class RecordRunService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}