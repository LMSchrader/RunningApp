package com.example.runningapp.worker

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.*
import com.example.runningapp.MainActivity
import com.example.runningapp.R
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
        private const val ID = 155556


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
            // remove old notifications
            (applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .cancel(ID)

            if (true) { // TODO
                showNotification()
            }

            return Result.success()
        } finally {
            runAt(applicationContext) // schedule for next day
        }
    }

    private fun showNotification() {
        val pendingIntent = NavDeepLinkBuilder(applicationContext)
            .setGraph(R.navigation.mobile_navigation)
            .setDestination(R.id.nav_record_run)
            .createPendingIntent()

        val builder = NotificationCompat.Builder(
            applicationContext,
            MainActivity.CHANNEL_ID_RUNNING_NOTIFICATIONS
        )
            .setSmallIcon(R.drawable.ic_baseline_directions_run_24)
            .setContentTitle(applicationContext.getString(R.string.runningDay))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(ID, builder.build())
        }
    }
}