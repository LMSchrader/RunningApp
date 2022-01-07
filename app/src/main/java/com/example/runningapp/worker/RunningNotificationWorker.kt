package com.example.runningapp.worker

import android.app.*
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

class RunningNotificationWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    companion object {
        private const val WORKER_NAME = "RunningNotification"
        private const val hour = 0
        private const val minute = 0

        const val CHANNEL_ID = "RunningNotification"
        const val TAG = "Worker"


        fun runAt(context: Context) {
            val workManager = WorkManager.getInstance(context)

            // trigger at specific not exact time
            val alarmTime = LocalTime.of(hour, minute)
            var currentDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)
            val currentTime = currentDateTime.toLocalTime()
            // if same time, schedule for next day as well
            // if today's time had passed, schedule for next day
            if (currentTime == alarmTime || currentTime.isAfter(alarmTime)) {
                currentDateTime = currentDateTime.plusDays(1)
            }
            currentDateTime = currentDateTime.withHour(alarmTime.hour).withMinute(alarmTime.minute)
            val duration = Duration.between(LocalDateTime.now(), currentDateTime)


            val workRequest = OneTimeWorkRequestBuilder<RunningNotificationWorker>()
                .setInitialDelay(duration.seconds, TimeUnit.SECONDS)
                .build()

            workManager.enqueueUniqueWork(WORKER_NAME, ExistingWorkPolicy.KEEP, workRequest)
        }

        fun cancel(context: Context) {
            val workManager = WorkManager.getInstance(context)
            workManager.cancelUniqueWork(WORKER_NAME)
        }
    }


    override fun doWork(): Result {
        try {
            // TODO
            createNotificationChannel()
            showNotification()

            return Result.success()
        } finally {
            runAt(applicationContext) // schedule for next day
        }
    }

    //TODO: notification
    // wird momentan nicht angezeigt
    private fun createNotificationChannel() {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        var notificationChannel = notificationManager.getNotificationChannel(CHANNEL_ID)
        if (notificationChannel == null) {
            notificationChannel = NotificationChannel(
                CHANNEL_ID, TAG, NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun showNotification() {
        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle("Test")
            .setContentText("Test")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(155556, builder.build())
        }
    }
}