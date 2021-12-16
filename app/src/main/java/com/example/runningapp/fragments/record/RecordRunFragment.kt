package com.example.runningapp.fragments.record

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.runningapp.R
import com.example.runningapp.databinding.FragmentRecordRunBinding
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.widget.TextView
import com.example.runningapp.services.RecordRunService

class RecordRunFragment : Fragment() {
    private var _binding: FragmentRecordRunBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { //permissions ->
        //when {
        //    permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
        //        // Precise location access granted.
        //    }
        //    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
        //        // Only approximate location access granted.
        //    }
        //    else -> {
        //        // No location access granted.
        //    }
        //}
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecordRunBinding.inflate(inflater, container, false)


        binding.startButton.setOnClickListener { startRun() }
        binding.stopButton.setOnClickListener { stopRun() }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startRun() {
        if (context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
            == PackageManager.PERMISSION_GRANTED) {
            //TODO
            context?.startForegroundService(Intent(context, RecordRunService::class.java))
        } else requestPermission()
    }

    private fun stopRun() {
        context?.stopService(Intent(context, RecordRunService::class.java))
    }

    private fun requestPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            showDialog()
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
    private fun showDialog() {
        val dialog = context?.let { Dialog(it) }
        dialog?.setContentView(R.layout.permission_dialog)
        dialog?.findViewById<TextView>(R.id.description)?.text = getString(R.string.location_permission_required)
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
}