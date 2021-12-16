package com.example.runningapp.services

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import java.util.concurrent.Executor
import java.util.concurrent.Executors.newSingleThreadExecutor

class RecordRunService : Service() {
    //TODO: Notification ueberarbeiten

    private val executor: Executor = newSingleThreadExecutor()

    companion object {
        const val CHANNEL_ID = "Job progress"
        const val TAG = "ForegroundWorker"
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        generateForegroundNotification()

        executor.execute {
            recordRun()
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRecordingRun()
    }

    private fun recordRun() {
        // TODO: implement
    }

    private fun stopRecordingRun() {
        // TODO: implement
    }

    private fun createNotificationChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        var notificationChannel = notificationManager.getNotificationChannel(CHANNEL_ID)
        if (notificationChannel == null) {
            notificationChannel = NotificationChannel(
                CHANNEL_ID, TAG, NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun generateForegroundNotification() {
        val pendingIntent: PendingIntent =
            Intent(this, RecordRunService::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

        val notification = Notification.Builder(application, CHANNEL_ID)
            .setContentTitle("Test")
            .setContentText("Test")
            .setContentIntent(pendingIntent)
            .build()

        startForeground(155555, notification)
    }
}