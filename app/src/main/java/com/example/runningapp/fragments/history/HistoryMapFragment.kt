package com.example.runningapp.fragments.history

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.runningapp.AppApplication
import com.example.runningapp.databinding.FragmentMapBinding
import com.example.runningapp.viewmodels.HistoryViewModel
import com.example.runningapp.viewmodels.HistoryViewModelFactory
import com.mapbox.geojson.Point
import com.mapbox.maps.*
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPolylineAnnotationManager
import com.example.runningapp.data.RunHistoryEntryMetaDataWithMeasurements
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationManager
import android.view.MotionEvent


class HistoryMapFragment : Fragment() {

    private val historyViewModel: HistoryViewModel by activityViewModels{
        HistoryViewModelFactory((activity?.application as AppApplication).runHistoryRepository)
    }

    lateinit var mapView: MapView
    lateinit var polylineAnnotationManager : PolylineAnnotationManager
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    lateinit var points : MutableList<Point>

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mapView = binding.mapView
        var interceptMove = false
        mapView.setOnTouchListener { view, motionEvent ->
            // Disallow the touch request for recyclerView scroll
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN ->            // See if we touch the screen borders
                    interceptMove =
                        100 * motionEvent.x > 5 * view.width && 100 * motionEvent.x < 95 * view.width
                MotionEvent.ACTION_MOVE -> if (interceptMove &&  view.parent != null) {
                view.parent.requestDisallowInterceptTouchEvent(true)
                }
            }
            false
        }
        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS)

        polylineAnnotationManager = mapView.annotations.createPolylineAnnotationManager()

        historyViewModel.currentRunHistoryEntryMetaDataWithMeasurements.observe(viewLifecycleOwner) { currentRunHistoryEntry ->
            points = extractAndTransformPointList(currentRunHistoryEntry)
            replaceRouteOnMap(points)
            setCameraPositionForMap(points)
        }

        mapView.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            setCameraPositionForMap(points)
        }

        //mapView.viewTreeObserver.addOnGlobalLayoutListener(
        //    object : OnGlobalLayoutListener {
        //        override fun onGlobalLayout() {
        //            // gets called after layout has been done but before display
        //            // so we can get the height then hide the view
        //            setCameraPositionForMap(points)
        //        }
        //    })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun replaceRouteOnMap(points :MutableList<Point>) {
        // Create an instance of the Annotation API and get the polyline manager.

        polylineAnnotationManager.deleteAll()

        // Set options for the resulting line layer.
        val polylineAnnotationOptions: PolylineAnnotationOptions = PolylineAnnotationOptions()
            .withPoints(points)
            // Style the line that will be added to the map.
            .withLineColor("#ee4e8b")
            .withLineWidth(5.0)
        // Add the resulting line to the map.
        polylineAnnotationManager.create(polylineAnnotationOptions)
    }

    private fun setCameraPositionForMap(points :MutableList<Point>) {
        if(points.isNotEmpty()) {
            val cameraOptions = CameraOptions.Builder().center(points[0]).build()
            mapView.getMapboxMap().setCamera(cameraOptions)


            val cameraPosition = mapView.getMapboxMap()
                .cameraForCoordinates(points, EdgeInsets(20.0, 20.0, 20.0, 20.0))
            // Set camera position
            mapView.getMapboxMap().setCamera(cameraPosition)
        }
    }

    private fun extractAndTransformPointList (currentRunHistoryEntryMetaDataWithMeasurements: RunHistoryEntryMetaDataWithMeasurements?) : MutableList<Point>{
        val points = mutableListOf<Point>()
        // Define a list of geographic coordinates to be connected.
        currentRunHistoryEntryMetaDataWithMeasurements?.measurements?.forEach{
            it.latitudeValue?.let { it1 -> it.longitudeValue?.let { it2 -> Point.fromLngLat(it2, it1) } }
                ?.let { it3 -> points.add(it3) }
        }
        return points
    }
}