package com.udacity.project4.ui.fragments.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.udacity.project4.R
import timber.log.Timber

class MapTypeBottomSheet : BottomSheetDialogFragment() {

    private var mapTypeSetCallback: ((Int) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.map_type_bottomsheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListeners(view)
    }

    fun onMapTypeSelectedCallback(callback: (Int) -> Unit) {
        mapTypeSetCallback = callback
    }

    private fun setTypeAndReturn(type: Int) {
        if (mapTypeSetCallback == null) {
            Timber.d("No Callback")
        } else {
            mapTypeSetCallback!!(type)
            dismiss()
        }
    }

    private fun initClickListeners(view: View) {
        val normalButton = view.findViewById<TextView>(R.id.normal_text_button)
        val sataliteButton = view.findViewById<TextView>(R.id.satalite_text_button)
        val hybridButton = view.findViewById<TextView>(R.id.hybrid_text_button)
        val terrainButton = view.findViewById<TextView>(R.id.terrain_text_button)
        normalButton.setOnClickListener { setTypeAndReturn(GoogleMap.MAP_TYPE_NORMAL) }
        sataliteButton.setOnClickListener { setTypeAndReturn(GoogleMap.MAP_TYPE_SATELLITE) }
        hybridButton.setOnClickListener { setTypeAndReturn(GoogleMap.MAP_TYPE_HYBRID) }
        terrainButton.setOnClickListener { setTypeAndReturn(GoogleMap.MAP_TYPE_TERRAIN) }
    }
}