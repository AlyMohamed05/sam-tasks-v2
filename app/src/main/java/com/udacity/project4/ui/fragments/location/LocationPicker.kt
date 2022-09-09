package com.udacity.project4.ui.fragments.location

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.udacity.project4.R
import com.udacity.project4.databinding.LocationPickerFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class LocationPicker : Fragment(),
    OnMapReadyCallback,
    GoogleMap.OnPoiClickListener,
    GoogleMap.OnMapLongClickListener {

    private val locationPickerViewModel by viewModel<LocationPickerViewModel>()

    private lateinit var binding: LocationPickerFragmentBinding
    private lateinit var map: GoogleMap

    private var selectedLocationMarker: Marker? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LocationPickerFragmentBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        initClickListeners()
        observe()
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        Timber.d("callback ")
        map = googleMap
        setMapStyle()
        map.isMyLocationEnabled = true
        map.uiSettings.isMyLocationButtonEnabled = true
        map.setOnPoiClickListener(this)
        map.setOnMapLongClickListener(this)
    }

    override fun onPoiClick(point: PointOfInterest) {
        locationPickerViewModel.setLocation(point.latLng)
    }

    override fun onMapLongClick(position: LatLng) {
        locationPickerViewModel.setLocation(position)
    }

    private fun setMapMarker(location: LatLng) {
        // First remove the current marker because there is only one position selected
        selectedLocationMarker?.remove()
        selectedLocationMarker = map.addMarker(
            MarkerOptions()
                .position(location)
        )
    }

    private fun setMapStyle() {
        try {
            map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style)
            )
        } catch (exception: Resources.NotFoundException) {
            Timber.d("Failed to set map style")
        }
    }

    private fun observe() {
        locationPickerViewModel.selectedLocation.observe(viewLifecycleOwner) { location ->
            if (location != null) {
                binding.fabSelectLocation.isEnabled = true
                setMapMarker(location)
            } else {
                binding.fabSelectLocation.isEnabled = false
            }
        }
    }

    private fun initClickListeners() {
        binding.apply {

            fabSelectLocation.setOnClickListener {
                parentFragmentManager.setFragmentResult(
                    "locationRequest",
                    bundleOf("location" to locationPickerViewModel.selectedLocation.value!!)
                )
                findNavController().navigateUp()
            }

            layerButton.setOnClickListener {
                val mapTypeBottomSheet = MapTypeBottomSheet()
                mapTypeBottomSheet.onMapTypeSelectedCallback { type ->
                    map.mapType = type
                }
                mapTypeBottomSheet.show(childFragmentManager,"mapTypeBottomSheet")
            }

        }
    }
}