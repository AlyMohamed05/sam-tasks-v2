package com.example.samtasks.ui.fragments.bottom_sheets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapSheetViewModel @Inject constructor() : ViewModel() {

    private val _selectedPosition = MutableLiveData<LatLng?>(null)
    val selectedPosition: LiveData<LatLng?>
        get() = _selectedPosition

    /**
     * Pass only position or point of interest
     * @throws IllegalStateException if no position or point of interest specified.
     */
    fun selectPosition(position: LatLng) {
        _selectedPosition.value = position
    }
}