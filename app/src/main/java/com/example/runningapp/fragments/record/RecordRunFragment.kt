package com.example.runningapp.fragments.record

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.runningapp.R
import com.example.runningapp.databinding.FragmentRecordRunBinding
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.example.runningapp.AppApplication
import com.example.runningapp.data.RunHistoryEntry
import com.example.runningapp.services.RecordRunService
import com.example.runningapp.viewmodels.RecordRunViewModel
import com.example.runningapp.viewmodels.RecordRunViewModelFactory
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import java.time.LocalDateTime
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class RecordRunFragment : Fragment() {

    private val recordRunViewModel: RecordRunViewModel by activityViewModels {
        RecordRunViewModelFactory((activity?.application as AppApplication).runHistoryRepository)
    }

    private var _binding: FragmentRecordRunBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
            }
            else -> {
                // No location access granted.
            }
        }
    }

    private val observerListener: (run: RunHistoryEntry?) -> Unit = { run ->
        if (run == null || run.timeValues.isEmpty()) {
            binding.currentTime.text = getString(R.string.time_empty)
            binding.currentKm.text = getString(R.string.value_empty)
            binding.avgPace.text = getString(R.string.value_empty)
            binding.currentPace.text = getString(R.string.value_empty)
        } else {
            binding.currentTime.text = run.timeValues[run.timeValues.lastIndex].toLong().toDuration(DurationUnit.NANOSECONDS).toString(DurationUnit.MINUTES, 2)
            binding.currentKm.text = "%.2f".format(run.kmRun)
            run.paceValues.removeAll(listOf(null))
            if (run.paceValues.isNotEmpty()) {
                binding.avgPace.text = "%.2f".format((run.paceValues as List<Float>).average())
                binding.currentPace.text = "%.2f".format(run.paceValues[run.paceValues.lastIndex])
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecordRunBinding.inflate(inflater, container, false)

        binding.currentTime.text = getString(R.string.time_empty)
        binding.currentKm.text = getString(R.string.value_empty)
        binding.avgPace.text = getString(R.string.value_empty)
        binding.currentPace.text = getString(R.string.value_empty)

        binding.startButton.setOnClickListener { startRun() }
        binding.stopButton.setOnClickListener { stopRun() }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startRun() {
        // check availability of Google Play Services
        if (context?.let {
                GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(it)
            } != ConnectionResult.SUCCESS) {
            showMissingGooglePlayServicesDialog()
            return
        }

        // check permissions
        if (context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
            == PackageManager.PERMISSION_GRANTED) {

            // check location settings
            checkLocationSettingsAndStartService(createLocationRequest())

        } else requestPermissions()
    }

    private fun stopRun() {
        context?.stopService(Intent(context, RecordRunService::class.java))

        recordRunViewModel.currentRun.removeObservers(viewLifecycleOwner)

        //TODO
        // nur einmal ausführen
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putInt(
                getString(R.string.kilometers_run),
                sharedPref.getInt(getString(R.string.kilometers_run), 0) + 10
            )
            apply()
        }

        //TODO
        val runningDay = true
        if (runningDay) {
            with(sharedPref.edit()) {
                putInt(
                    getString(R.string.running_days_kept),
                    sharedPref.getInt(getString(R.string.running_days_kept), 0) + 1
                )
                apply()
            }
        }
    }

    private fun requestPermissions() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            showPermissionDialog()
        } else {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    /**
     * Opens a dialog, that explains why the permissions are needed and asks for the permissions afterwards.
     */
    private fun showPermissionDialog() {
        val dialog = context?.let { Dialog(it) }
        dialog?.setContentView(R.layout.permission_dialog)
        dialog?.findViewById<TextView>(R.id.description)?.text =
            getString(R.string.location_permission_required)
        val btn: TextView? = dialog?.findViewById(R.id.button)
        btn?.setOnClickListener {
            dialog.dismiss()
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
        dialog?.show()
    }

    private fun showMissingGooglePlayServicesDialog() {
        val dialog = context?.let { Dialog(it) }
        dialog?.setContentView(R.layout.permission_dialog)
        dialog?.findViewById<TextView>(R.id.description)?.text =
            getString(R.string.google_play_services_missing)

        val btn: TextView? = dialog?.findViewById(R.id.button)

        btn?.text = getString(R.string.ok)

        btn?.setOnClickListener {
            dialog.dismiss()
        }
        dialog?.show()
    }

    private fun createLocationRequest(): LocationRequest {
        return LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun checkLocationSettingsAndStartService(locationRequest: LocationRequest) {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client: SettingsClient? = context?.let { LocationServices.getSettingsClient(it) }
        val task: Task<LocationSettingsResponse>? = client?.checkLocationSettings(builder.build())

        task?.addOnSuccessListener {
            // All location settings are satisfied. The client can initialize
            // location requests here.
            val currentTime = LocalDateTime.now()
            recordRunViewModel.insertAndObserve(RunHistoryEntry(currentTime),viewLifecycleOwner, observerListener)

            //TODO: Forground service kann mehrmals gestatrtet werden (dann wird onStartCommand erneut aufgerufen)
            context?.startForegroundService(Intent(context, RecordRunService()::class.java).putExtra("id",currentTime.toString()))
        }

        task?.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    activity?.let {
                        exception.startResolutionForResult(
                            it,
                            100 // 100: GPS setting // TODO, anscheinend random wert
                        )
                    }
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }
}