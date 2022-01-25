package com.example.runningapp.services

import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.os.Looper
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkBuilder
import com.example.runningapp.AppApplication
import com.example.runningapp.MainActivity
import com.example.runningapp.R
import com.example.runningapp.broadcastReceiver.StopRunBroadcastReceiver
import com.example.runningapp.data.RunHistoryEntry
import com.example.runningapp.data.RunHistoryRepository
import com.example.runningapp.data.RunningScheduleRepository
import com.example.runningapp.util.DateUtil
import com.google.android.gms.location.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.concurrent.Executor
import java.util.concurrent.Executors.newSingleThreadExecutor
import kotlin.math.pow

class RecordRunService: LifecycleService() {
    private val executor: Executor = newSingleThreadExecutor() // TODO: ist executor noch notwendig

    private lateinit var sharedPref: SharedPreferences

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private var lastLocation: Location? = null
    private var startTime: Long? = null

    private lateinit var runHistoryRepository: RunHistoryRepository
    private lateinit var runningScheduleRepository: RunningScheduleRepository
    private lateinit var run : RunHistoryEntry

    companion object {
        private const val ID = 155555

        fun createLocationRequest(): LocationRequest {
            return LocationRequest.create().apply {
                interval = 10000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
        }
    }


    // TODO: onCreate on startCommand (wo muss was hin)
    override fun onCreate() {
        super.onCreate()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = createLocationRequest()

        runHistoryRepository = (application as AppApplication).runHistoryRepository
        runningScheduleRepository = (application as AppApplication).runningScheduleRepository
        sharedPref = (application as AppApplication).shardPref
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        generateForegroundNotification()

        executor.execute {
            if (intent != null) {
                lifecycleScope.launch {
                    run = runHistoryRepository.get(LocalDateTime.parse(intent.extras?.get("id") as CharSequence?))
                    recordRun()
                }
            }
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRecordingRun()
    }

    private suspend fun recordRun() {
        startTime = SystemClock.elapsedRealtimeNanos()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {
                        if (lastLocation != null) {
                            val runKm = location.distanceTo(lastLocation)/1000
                            run.kmRun += runKm
                            run.timeValues.add((location.elapsedRealtimeNanos- startTime!!).toFloat())
                            run.altitudeValues.add(location.altitude.toFloat())
                            if (location.speed == 0.0F) {
                                run.paceValues.add(null)
                            } else {
                                run.paceValues.add(
                                    (location.speed * 0.06F).toDouble().pow((-1).toDouble())
                                        .toFloat()
                                )
                            }

                            // update kilometers run counter
                            with(sharedPref.edit()) {
                                putFloat(
                                    getString(R.string.kilometers_run_preferences),
                                    sharedPref.getFloat(getString(R.string.kilometers_run_preferences),
                                        0.0F
                                    ) + runKm
                                )
                                apply()
                            }
                        } else {
                            if ((startTime as Long) < location.elapsedRealtimeNanos) {
                                startTime = location.elapsedRealtimeNanos
                            } else {
                                continue
                            }
                        }
                        lastLocation = location
                    }
                lifecycleScope.launch {
                    runHistoryRepository.update(run)
                }
            }
        }


        // update counter running days kept
        val lastRunningDay = sharedPref.getString(getString(R.string.last_running_day_preferences), "")
        if ((lastRunningDay == "" || !LocalDateTime.parse(lastRunningDay).toLocalDate().equals(DateUtil.StaticFunctions.getTodaysDate())) && runningScheduleRepository.isTodayARunningDayOneTimeRequest()) {
            with(sharedPref.edit()) {
                putInt(
                    getString(R.string.running_days_kept_preferences),
                    sharedPref.getInt(getString(R.string.running_days_kept_preferences), 0) + 1
                )
                apply()
            }

            with(sharedPref.edit()) {
                putString(
                    getString(R.string.last_running_day_preferences),
                    run.date.toString()
                )
                apply()
            }
        }

        //getLastLocation()

        startLocationUpdates()
    }

    private fun stopRecordingRun() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)

        // mark service as stopped
        with(sharedPref.edit()) {
            putString(
                getString(R.string.service_active_preferences),
                ""
            )
            apply()
        }
    }

    private fun generateForegroundNotification() {
        val pendingIntent = NavDeepLinkBuilder(this)
            .setGraph(R.navigation.mobile_navigation)
            .setDestination(R.id.nav_record_run)
            .createPendingIntent()

        val stopRunIntent = Intent(this, StopRunBroadcastReceiver::class.java)
        val stopRunPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 0, stopRunIntent, 0)

        val notification = NotificationCompat.Builder(this, MainActivity.CHANNEL_ID_SERVICE)
            .setSmallIcon(R.drawable.ic_baseline_directions_run_24)
            .setContentTitle(getString(R.string.record_run_notification_title))
            .setContentText(getString(R.string.record_run_notification_text))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_LOCATION_SHARING)
            .addAction(0, getString(R.string.stop),
                stopRunPendingIntent)
            .build()

        startForeground(ID, notification)
    }

    private fun getLastLocation() {
        try {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener()
            { location: Location? -> //TODO
                // Got last known location. In some rare situations this can be null.
            }
        } catch(e: SecurityException) {

        }
    }

    private fun startLocationUpdates() {
        try {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.getMainLooper()
            )
        } catch (unlikely: SecurityException) {
        }
    }

}