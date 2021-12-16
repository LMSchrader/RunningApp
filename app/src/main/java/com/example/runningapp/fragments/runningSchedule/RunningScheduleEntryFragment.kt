package com.example.runningapp.fragments.runningSchedule

import android.Manifest
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.runningapp.R
import com.example.runningapp.AppApplication
import com.example.runningapp.databinding.FragmentRunningScheduleEntryBinding
import com.example.runningapp.util.OrientationUtil.StaticFunctions.isLandscapeMode
import com.example.runningapp.viewmodels.RunningScheduleViewModel
import com.example.runningapp.viewmodels.RunningScheduleViewModelFactory
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.AdvertisingOptions
import com.google.android.gms.nearby.connection.Strategy.P2P_POINT_TO_POINT
import com.google.android.gms.nearby.connection.DiscoveryOptions
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes
import com.google.android.gms.nearby.connection.ConnectionResolution
import com.google.android.gms.nearby.connection.ConnectionInfo
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadCallback

class RunningScheduleEntryFragment : Fragment() {
    private val viewModel: RunningScheduleViewModel by activityViewModels {
        RunningScheduleViewModelFactory((activity?.application as AppApplication).runningScheduleRepository)
    }
    private var _binding: FragmentRunningScheduleEntryBinding? = null

    private val binding get() = _binding!!

    private var REQUIRED_PERMISSIONS : Array<String>
    init {
        if (Build.VERSION.SDK_INT >= 29) {
            REQUIRED_PERMISSIONS = arrayOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            REQUIRED_PERMISSIONS = arrayOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // in landscape mode this fragment should not be displayed alone
        if (context?.let { isLandscapeMode(it) } == true && parentFragmentManager.findFragmentById(R.id.leftFragment) == null) {
            findNavController().popBackStack()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRunningScheduleEntryBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        viewModel.currentEntry.observe(viewLifecycleOwner) { currentEntry ->
            if (currentEntry != null) {
                binding.title.text = currentEntry.title
                binding.weekdays.text = context?.let { currentEntry.getWeekdayString(it) }
                binding.startDate.text = currentEntry.startDate.toString()
                binding.endDate.text = currentEntry.endDate.toString()
                binding.description.text = currentEntry.description
            } else if (parentFragmentManager.findFragmentById(R.id.rightFragment) != null) {
                (parentFragment as RunningScheduleFragment).removeSecondFragment()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_running_schedule_entry, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.imageShare) {
            if (hasPermissions()) {
                startAdvertising()
                startDiscovery()
            } else {
                requestPermissions()
            }
            return true
        }

        if (context?.let { isLandscapeMode(it) } == true) {
            return when (item.itemId) {
                R.id.imageEdit -> {
                    view?.findNavController()
                        ?.navigate(R.id.action_nav_running_schedule_to_nav_edit_running_schedule_entry)
                    true
                }

                R.id.imageDelete -> {
                    viewModel.currentEntry.value?.let { viewModel.delete(it) }
                    true
                }

                else -> super.onOptionsItemSelected(item)
            }
        } else {
            return when (item.itemId) {
                R.id.home -> {
                    activity?.onBackPressed()
                    return true
                }

                R.id.imageEdit -> {
                    view?.findNavController()
                        ?.navigate(R.id.action_nav_running_schedule_entry_to_nav_edit_running_schedule_entry)
                    true
                }

                R.id.imageDelete -> {
                    viewModel.currentEntry.value?.let { viewModel.delete(it) }
                    activity?.onBackPressed()
                    true
                }

                else -> super.onOptionsItemSelected(item)
            }
        }
    }

    private fun hasPermissions(): Boolean {
        for (permission in REQUIRED_PERMISSIONS) {
            if (context?.let { ContextCompat.checkSelfPermission(it, permission) }
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    private fun shouldShowRequestPermissionRationale(): Boolean {
        for (permission in REQUIRED_PERMISSIONS) {
            if (shouldShowRequestPermissionRationale(permission)) {
                return true
            }
        }
        return false
    }


    private fun requestPermissions() {
        if (shouldShowRequestPermissionRationale()) {
            showDialog()
        } else {
            locationPermissionRequest.launch(REQUIRED_PERMISSIONS)
        }
    }

    /**
     * Opens a dialog, that explains why the permissions are needed and asks for the permissions afterwards.
     */

    private fun showDialog() {
        val dialog = context?.let { Dialog(it) }
        dialog?.setContentView(R.layout.permission_dialog)
        dialog?.findViewById<TextView>(R.id.description)?.text = getString(R.string.location_permission_required_google_nearby)
        val btn: TextView? = dialog?.findViewById(R.id.button)
        btn?.setOnClickListener {
            dialog.dismiss()
            locationPermissionRequest.launch(REQUIRED_PERMISSIONS)
        }
        dialog?.show()
    }

    private fun startAdvertising() {
        val advertisingOptions: AdvertisingOptions =
            AdvertisingOptions.Builder().setStrategy(P2P_POINT_TO_POINT).build()
        context?.let {
            Nearby.getConnectionsClient(it)
                .startAdvertising(
                    Settings.Global.getString(it.contentResolver, "device_name"),
                    it.packageName,
                    connectionLifecycleCallback,
                    advertisingOptions
                )
                .addOnSuccessListener { unused: Void? -> }
                .addOnFailureListener { e: Exception? -> }
        }
    }

    private fun startDiscovery() {
        val discoveryOptions = DiscoveryOptions.Builder().setStrategy(P2P_POINT_TO_POINT).build()
        context?.let {
            Nearby.getConnectionsClient(it)
                .startDiscovery(it.packageName, endpointDiscoveryCallback, discoveryOptions)
                .addOnSuccessListener { unused: Void? -> }
                .addOnFailureListener { e: java.lang.Exception? -> }
        }
    }

    private val endpointDiscoveryCallback: EndpointDiscoveryCallback =
        object : EndpointDiscoveryCallback() {
            override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
                // An endpoint was found. We request a connection to it.
                context?.let {
                    Nearby.getConnectionsClient(it)
                        .requestConnection(
                            Settings.Global.getString(
                                it.contentResolver,
                                "device_name"
                            ), endpointId, connectionLifecycleCallback
                        )
                        .addOnSuccessListener(
                            OnSuccessListener { unused: Void? -> })
                        .addOnFailureListener(
                            OnFailureListener { e: java.lang.Exception? -> })
                }
            }

            override fun onEndpointLost(endpointId: String) {
                // A previously discovered endpoint has gone away.
            }
        }

    private val connectionLifecycleCallback: ConnectionLifecycleCallback =
        object : ConnectionLifecycleCallback() {
            override fun onConnectionInitiated(endpointId: String, connectionInfo: ConnectionInfo) {
                context?.let {
                    AlertDialog.Builder(it)
                        .setTitle("Accept connection to " + connectionInfo.getEndpointName())
                        .setMessage("Confirm the code matches on both devices: " + connectionInfo.getAuthenticationDigits())
                        .setPositiveButton(
                            "Accept"
                        ) { dialog: DialogInterface?, which: Int ->  // The user confirmed, so we can accept the connection.
                            Nearby.getConnectionsClient(context!!)
                                .acceptConnection(endpointId, ReceiveBytesPayloadListener())
                        }
                        .setNegativeButton(
                            android.R.string.cancel
                        ) { dialog: DialogInterface?, which: Int ->  // The user canceled, so we should reject the connection.
                            Nearby.getConnectionsClient(context!!).rejectConnection(endpointId)
                        }
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show()
                }
            }

            override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
                when (result.status.statusCode) {
                    ConnectionsStatusCodes.STATUS_OK -> {
                        val bytesPayload = Payload.fromBytes(byteArrayOf(0xa, 0xb, 0xc, 0xd))
                        Nearby.getConnectionsClient(context!!)
                            .sendPayload(endpointId, bytesPayload)
                    }
                    ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED -> {
                    }
                    ConnectionsStatusCodes.STATUS_ERROR -> {
                    }
                    else -> {
                    }
                }
            }

            override fun onDisconnected(endpointId: String) {
                // We've been disconnected from this endpoint. No more data can be
                // sent or received.
            }
        }

    internal class ReceiveBytesPayloadListener : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            // This always gets the full data of the payload. Is null if it's not a BYTES payload.
            if (payload.type == Payload.Type.BYTES) {
                val receivedBytes = payload.asBytes()
            }
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
            // Bytes payloads are sent as a single chunk, so you'll receive a SUCCESS update immediately
            // after the call to onPayloadReceived().
        }
    }
}