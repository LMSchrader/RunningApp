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
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.fragment.app.*
import com.example.runningapp.AppApplication
import com.example.runningapp.data.RunHistoryEntry
import com.example.runningapp.fragments.dialogs.CustomDialogFragment
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
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class RecordRunFragment : Fragment(), CustomDialogFragment.CustomDialogListener {
    private val recordRunViewModel: RecordRunViewModel by activityViewModels {
        RecordRunViewModelFactory((activity?.application as AppApplication).runHistoryRepository)
    }

    private var _binding: FragmentRecordRunBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var sharedPref: SharedPreferences
    private lateinit var prefListener: OnSharedPreferenceChangeListener

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
        } else { // TODO: überarbeiten
            binding.currentTime.text = run.timeValues[run.timeValues.lastIndex].toLong()
                .toDuration(DurationUnit.NANOSECONDS).toString(DurationUnit.MINUTES, 2)
            binding.currentKm.text = "%.2f".format(run.kmRun)

            //val pace = run.paceValues[run.paceValues.lastIndex] //TODO: hier kommt es manchmal zu ArrayIndexOutOfBoundsException und infinit exeptions
            //if (pace != null) {
            //    binding.currentPace.text = "%.2f".format(pace)
            //} else {
                binding.currentPace.text = getString(R.string.value_empty)
            //}
//
            //run.paceValues.removeAll(listOf(null))
            //if (run.paceValues.isNotEmpty()) {
            //    binding.avgPace.text = "%.2f".format((run.paceValues as List<Float>).average())
            //} else {
            binding.avgPace.text = getString(R.string.value_empty)
            //}
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)!!
        prefListener = OnSharedPreferenceChangeListener { prefs, key ->
            if (key == getString(R.string.service_active)) {
                val date = sharedPref.getString(getString(R.string.service_active), "")

                if (date != "") {
                    binding.startButton.visibility = View.GONE
                    binding.stopButton.visibility = View.VISIBLE
                } else {
                    binding.startButton.visibility = View.VISIBLE
                    binding.stopButton.visibility = View.GONE
                }
            }
        }

        sharedPref.registerOnSharedPreferenceChangeListener(prefListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecordRunBinding.inflate(inflater, container, false)

        binding.startButton.setOnClickListener { startRun() }
        binding.stopButton.setOnClickListener { stopRun() }


        val date = sharedPref.getString(getString(R.string.service_active), "")

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

    override fun onDestroyView() {
        super.onDestroyView()
        sharedPref.unregisterOnSharedPreferenceChangeListener(prefListener)
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

    private fun stopRun() { // TODO: Alles auslager in service, da es nicht vom broadcast receiver aufgerufen wird
        context?.stopService(Intent(context, RecordRunService::class.java))

        recordRunViewModel.removeObserver(viewLifecycleOwner)

        //TODO: auslagern in service
        with(sharedPref.edit()) {
            putString(
                getString(R.string.service_active),
                ""
            )
            apply()
        }


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
        val dialog = CustomDialogFragment.getInstance(getString(R.string.location_permission_required))
        dialog.show(childFragmentManager, CustomDialogFragment.TAG)
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
                RunHistoryEntry(currentTime),
                viewLifecycleOwner,
                observerListener
            )


            with(sharedPref.edit()) {
                putString(
                    getString(R.string.service_active),
                    currentTime.toString()
                )
                apply()
            }


            // start service
            context?.startForegroundService(
                Intent(
                    context,
                    RecordRunService()::class.java
                ).putExtra("id", currentTime.toString()) //TODO: nicht mehr nötig -> in sharedPreferences
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