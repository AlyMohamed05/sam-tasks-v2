package com.example.samtasks.ui.fragments.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.samtasks.databinding.CreateTaskFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateTaskFragment: Fragment() {

    private lateinit var binding: CreateTaskFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CreateTaskFragmentBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }
}