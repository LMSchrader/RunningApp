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
import kotlin.math.floor
import kotlin.math.pow
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class RecordRunFragment : Fragment(), ContinueDialogFragment.CustomDialogListener {
    private val recordRunViewModel: RecordRunViewModel by activityViewModels {
        RecordRunViewModelFactory((activity?.application as AppApplication).runHistoryRepository)
    }

    private var _binding: FragmentRecordRunBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var sharedPref: SharedPreferences
    private var prefListener: OnSharedPreferenceChangeListener = OnSharedPreferenceChangeListener { _, key ->
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

    private val observerListener: (run: RunHistoryEntryMetaDataWithMeasurements?) -> Unit = { run ->
        if (run == null || run.measurements.isEmpty()) {
            binding.currentTime.text = getString(R.string.time_empty)
            binding.currentKm.text = getString(R.string.value_empty)
            binding.avgPace.text = getString(R.string.value_empty)
            binding.currentPace.text = getString(R.string.value_empty)
        } else {
            binding.currentTime.text = floor(run.measurements[run.measurements.lastIndex].timeValue.toLong().div(10.toDouble().pow(9)))
                .toDuration(DurationUnit.SECONDS).toString()
            binding.currentKm.text = "%.2f".format(run.metaData.kmRun)

            val pace =
                run.measurements[run.measurements.lastIndex].paceValue
            if (pace != null) {
                if(pace.toLong().toDuration(DurationUnit.MINUTES).inWholeDays > 1) {
                    binding.avgPace.text = getString(R.string.value_empty)
                } else {
                    if(pace.toLong().toDuration(DurationUnit.MINUTES).inWholeHours > 1) {
                        binding.currentPace.text =
                            pace.div(60).toLong().toDuration(DurationUnit.MINUTES)
                                .toString()
                    } else {
                        binding.currentPace.text =
                            pace.times(60).toLong().toDuration(DurationUnit.SECONDS)
                                .toString()
                    }
                }
            } else {
                binding.currentPace.text = getString(R.string.value_empty)
            }

            if (run.measurements.isNotEmpty()) {
                var sum = 0F
                var count = 1
                run.measurements.forEach{
                    if(it.paceValue != null) {
                        sum+=it.paceValue!!
                        count++
                    }
                }
                val average = sum/count
                if(average.toLong().toDuration(DurationUnit.MINUTES).inWholeDays > 1) {
                    binding.avgPace.text = getString(R.string.value_empty)
                } else {
                    if(average.toLong().toDuration(DurationUnit.MINUTES).inWholeHours > 1) {
                        binding.avgPace.text =
                            average.div(60).toLong().toDuration(DurationUnit.MINUTES)
                                .toString()
                    } else {
                        binding.avgPace.text =
                            average.times(60).toLong().toDuration(DurationUnit.SECONDS)
                                .toString()
                    }
                }
            } else {
                binding.avgPace.text = getString(R.string.value_empty)
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
        val dialog = ContinueDialogFragment.getInstance(getString(R.string.location_permission_required))
        dialog.show(childFragmentManager, ContinueDialogFragment.TAG)
    }

    private fun showMissingGooglePlayServicesDialog() {
        val dialog = NoteDialogFragment.getInstance(getString(R.string.google_play_services_missing))
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
                RunHistoryEntryMetaDataWithMeasurements(RunHistoryEntryMetaData(currentTime), mutableListOf()),
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
                            100 // 100: GPS setting // TODO, anscheinend random wert
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