package com.example.runningapp.fragments.record

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.runningapp.R
import com.example.runningapp.databinding.FragmentRecordRunBinding
import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.fragment.app.*
import com.example.runningapp.AppApplication
import com.example.runningapp.data.RunHistoryEntryMetaDataWithMeasurements
import com.example.runningapp.data.RunHistoryEntryMetaData
import com.example.runningapp.fragments.dialogs.ContinueDialogFragment
import com.example.runningapp.fragments.dialogs.NoteDialogFragment
import com.example.runningapp.services.RecordRunService
import com.example.runningapp.viewmodels.RecordRunViewModel
import com.example.runningapp.viewmodels.RecordRunViewModelFactory
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import java.time.LocalDateTime

class RecordRunFragment : Fragment(), ContinueDialogFragment.ContinueDialogListener {
    private val recordRunViewModel: RecordRunViewModel by activityViewModels {
        RecordRunViewModelFactory((activity?.application as AppApplication).runHistoryRepository)
    }

    private var _binding: FragmentRecordRunBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPref: SharedPreferences
    private var prefListener: OnSharedPreferenceChangeListener =
        OnSharedPreferenceChangeListener { _, key ->
            if (key == getString(R.string.service_active_preferences)) {
                val date = sharedPref.getString(getString(R.string.service_active_preferences), "")

                if (date != "") {
                    binding.startButton.visibility = View.GONE
                    binding.stopButton.visibility = View.VISIBLE
                } else {
                    binding.startButton.visibility = View.VISIBLE
                    binding.stopButton.visibility = View.GONE
                }
            }
        }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { _ -> }

    private val observerListener: (run: RunHistoryEntryMetaDataWithMeasurements?) -> Unit = { run ->
        if (run == null || run.measurements.isEmpty()) {
            binding.currentTime.text = getString(R.string.time_empty)
            binding.currentKm.text = getString(R.string.value_empty)
            binding.avgPace.text = getString(R.string.value_empty)
            binding.currentPace.text = getString(R.string.value_empty)
        } else {
            binding.currentTime.text = run.metaData.getTimeRunAsString()
            binding.currentKm.text = run.metaData.getKmRunAsString()

            if (run.measurements[run.measurements.lastIndex].getPaceValueAsString().isEmpty()) {
                binding.currentPace.text = getString(R.string.value_empty)
            } else {
                binding.currentPace.text =
                    run.measurements[run.measurements.lastIndex].getPaceValueAsString()
            }

            if (run.getAveragePaceAsString().isEmpty()) {
                binding.avgPace.text = getString(R.string.value_empty)
            } else {
                binding.avgPace.text = run.getAveragePaceAsString()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = (activity?.application as AppApplication).shardPref
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecordRunBinding.inflate(inflater, container, false)

        binding.startButton.setOnClickListener { startRun() }
        binding.stopButton.setOnClickListener { stopRun() }


        val date = sharedPref.getString(getString(R.string.service_active_preferences), "")

        // if service is active, show stop button
        if (!date.isNullOrEmpty()) {
            binding.startButton.visibility = View.GONE
            binding.stopButton.visibility = View.VISIBLE

            // update view model if necessary and observe current run
            if (recordRunViewModel.currentRun.value == null) {
                recordRunViewModel.setCurrentRunAndObserve(
                    LocalDateTime.parse(date as CharSequence?), viewLifecycleOwner, observerListener
                )
            } else {
                recordRunViewModel.currentRun.observe(viewLifecycleOwner, observerListener)
            }
        } else {
            // if service is not active, show default
            binding.currentTime.text = getString(R.string.time_empty)
            binding.currentKm.text = getString(R.string.value_empty)
            binding.avgPace.text = getString(R.string.value_empty)
            binding.currentPace.text = getString(R.string.value_empty)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        sharedPref.registerOnSharedPreferenceChangeListener(prefListener)
    }

    override fun onPause() {
        super.onPause()
        sharedPref.unregisterOnSharedPreferenceChangeListener(prefListener)
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
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions()
            return
        }

        // check location settings
        checkLocationSettingsAndStartService(RecordRunService.createLocationRequest())
    }

    private fun stopRun() {
        context?.stopService(Intent(context, RecordRunService::class.java))

        recordRunViewModel.removeObserver(viewLifecycleOwner)

        // mark service as stopped
        with(sharedPref.edit()) {
            putString(
                getString(R.string.service_active_preferences),
                ""
            )
            apply()
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
        val dialog =
            ContinueDialogFragment.getInstance(getString(R.string.location_permission_required))
        dialog.show(childFragmentManager, ContinueDialogFragment.TAG)
    }

    private fun showMissingGooglePlayServicesDialog() {
        val dialog =
            NoteDialogFragment.getInstance(getString(R.string.google_play_services_missing))
        dialog.show(childFragmentManager, NoteDialogFragment.TAG)
    }

    private fun checkLocationSettingsAndStartService(locationRequest: LocationRequest) {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client: SettingsClient? = context?.let { LocationServices.getSettingsClient(it) }
        val task: Task<LocationSettingsResponse>? = client?.checkLocationSettings(builder.build())

        task?.addOnSuccessListener {
            // create object
            val currentTime = LocalDateTime.now()
            recordRunViewModel.insertAndObserve(
                RunHistoryEntryMetaDataWithMeasurements(
                    RunHistoryEntryMetaData(currentTime),
                    mutableListOf()
                ),
                viewLifecycleOwner,
                observerListener
            )


            // mark service as active
            with(sharedPref.edit()) {
                putString(
                    getString(R.string.service_active_preferences),
                    currentTime.toString()
                )
                apply()
            }


            // start service
            context?.startForegroundService(
                Intent(
                    context,
                    RecordRunService()::class.java
                ).putExtra("id", currentTime.toString())
            )
        }

        task?.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    activity?.let {
                        exception.startResolutionForResult(
                            it,
                            100
                        )
                    }
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
}