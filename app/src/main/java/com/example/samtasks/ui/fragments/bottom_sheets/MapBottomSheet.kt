package com.example.samtasks.ui.fragments.bottom_sheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.samtasks.R
import com.example.samtasks.utils.animateIntoScreen
import com.example.samtasks.utils.animateOutScreen
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PointOfInterest
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MapBottomSheet : BottomSheetDialogFragment(),
    OnMapReadyCallback,
    GoogleMap.OnPoiClickListener,
    GoogleMap.OnMapLongClickListener {

    private val viewModel: MapSheetViewModel by viewModels()

    private lateinit var map: GoogleMap
    private lateinit var setLocationButton: FloatingActionButton

    /**
     * Called when user submits a location to add to the task
     * Submission is done by FAB button
     */
    private var locationSetCallback: ((LatLng) -> Unit)? = null

    /**
     * Called in onPause() to save the current map state
     * The host [Activity,fragment] must provide this call back to avoid
     * losing the state of the map when the sheet disappears.
     */
    private var saveMapStateCallback: (() -> Unit)? = null

    private var selectedPositionMarker: Marker? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.map_bottom_sheet,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLocationButton = view.findViewById(R.id.set_location_fab)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        initClickListeners()
        observe()
    }

    override fun onPause() {
        super.onPause()
        if (saveMapStateCallback == null) {
            Timber.d("saveMapStateCallback is not set\nMap will lose it's state.")
        } else {
            saveMapStateCallback!!()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnPoiClickListener(this)
        map.setOnMapLongClickListener(this)
    }

    override fun onPoiClick(point: PointOfInterest) {
        viewModel.selectPosition(point.latLng)
    }

    override fun onMapLongClick(position: LatLng) {
        viewModel.selectPosition(position)
    }

    fun addSetLocationCallback(callback: (LatLng) -> Unit) {
        locationSetCallback = callback
    }

    private fun setMapMarker(location: LatLng) {
        // First remove the current marker because there is only one position selected
        selectedPositionMarker?.remove()
        selectedPositionMarker = map.addMarker(
            MarkerOptions()
                .position(location)
        )
    }

    private fun initClickListeners() {
        setLocationButton.setOnClickListener {
            if (locationSetCallback == null) {
                Timber.d("No registered callback")
            } else {
                locationSetCallback!!(viewModel.selectedPosition.value!!)
                dismiss()
            }
        }
    }

    /**
     * Observes viewModel livedata.
     */
    private fun observe() {
        viewModel.apply {

            selectedPosition.observe(this@MapBottomSheet) { position ->
                if (position != null) {
                    if (setLocationButton.visibility != View.VISIBLE) {
                        setLocationButton.animateIntoScreen()
                    }
                    setMapMarker(position)
                } else {
                    if (setLocationButton.visibility == View.VISIBLE) {
                        setLocationButton.animateOutScreen()
                    }
                }
            }

        }
    }

}