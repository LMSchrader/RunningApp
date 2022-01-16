package com.example.runningapp.services

import android.content.Intent
import android.location.Location
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkBuilder
import com.example.runningapp.AppApplication
import com.example.runningapp.MainActivity
import com.example.runningapp.R
import com.example.runningapp.data.RunHistoryEntry
import com.example.runningapp.data.RunHistoryRepository
import com.google.android.gms.location.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.concurrent.Executor
import java.util.concurrent.Executors.newSingleThreadExecutor
import kotlin.math.pow

class RecordRunService: LifecycleService() {
    private val executor: Executor = newSingleThreadExecutor() // TODO: ist executor noch notwendig

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private var lastLocation: Location? = null
    private var startTime: Long? = null

    private lateinit var runHistoryRepository: RunHistoryRepository
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


    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    // TODO: onCreate on startCommand (wo muss was hin)
    override fun onCreate() {
        super.onCreate()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = createLocationRequest()

        runHistoryRepository = (application as AppApplication).runHistoryRepository
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

    private fun recordRun() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {
                        if (lastLocation != null) {
                            run.kmRun += location.distanceTo(lastLocation)/1000
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
                        } else {
                            startTime = location.elapsedRealtimeNanos
                        }
                        lastLocation = location
                    }
                lifecycleScope.launch {
                    runHistoryRepository.update(run)
                }
            }
        }

        //getLastLocation()

        startLocationUpdates()
    }

    private fun stopRecordingRun() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun generateForegroundNotification() { //TODO: eventuell stop button einbauen
        val pendingIntent = NavDeepLinkBuilder(this)
            .setGraph(R.navigation.mobile_navigation)
            .setDestination(R.id.nav_record_run)
            .createPendingIntent()

        val notification = NotificationCompat.Builder(this, MainActivity.CHANNEL_ID_SERVICE)
            .setSmallIcon(R.drawable.ic_baseline_directions_run_24)
            .setContentTitle(getString(R.string.record_run_notification_title))
            .setContentText(getString(R.string.record_run_notification_text))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_LOCATION_SHARING)
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