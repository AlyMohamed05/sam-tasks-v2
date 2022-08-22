package com.example.samtasks.ui.fragments.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.samtasks.databinding.LocationFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationFragment: Fragment() {

    private lateinit var binding: LocationFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LocationFragmentBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }
}