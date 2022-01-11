package com.example.runningapp.services

import android.app.*
import android.content.Intent
import android.location.Location
import android.os.IBinder
import android.os.Looper
import com.example.runningapp.AppApplication
import com.example.runningapp.data.RunHistoryRepository
import com.google.android.gms.location.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors.newSingleThreadExecutor

class RecordRunService : Service() {
    //TODO: Notification ueberarbeiten

    private val executor: Executor = newSingleThreadExecutor()

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    // Used only for local storage of the last known location. Usually, this would be saved to your
    // database, but because this is a simplified sample without a full database, we only need the
    // last location to create a Notification if the user navigates away from the app.
    private var currentLocation: Location? = null //TODO
    private lateinit var runHistoryRepository: RunHistoryRepository

    companion object {
        const val CHANNEL_ID = "Job progress"
        const val TAG = "ForegroundWorker"
    }


    override fun onBind(intent: Intent?): IBinder? {
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
        //locationRequest = createLocationRequest()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                //runHistoryRepository.get() // todo: hier aktuelle daten lesen

                // Normally, you want to save a new location to a database. We are simplifying
                // things a bit and just saving it as a local variable, as we only need it again
                // if a Notification is created (when the user navigates away from app).
                for (location in locationResult.locations){
                    currentLocation = location //TODO: sammeln der neuen daten?

                }
                //runHistoryRepository.update() // todo: hier wegspeichern

            }
        }

        getLastLocation()

        startLocationUpdates()
    }

    private fun stopRecordingRun() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
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

    private fun createLocationRequest(): LocationRequest {
        return LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
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