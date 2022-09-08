package com.udacity.project4.ui.fragments.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class LocationPickerViewModel: ViewModel() {

    private val _selectedLocation = MutableLiveData<LatLng?>(null)
     val selectedLocation: LiveData<LatLng?>
        get() = _selectedLocation

    fun setLocation(location: LatLng){
        _selectedLocation.value = location
    }
}