package com.example.samtasks.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.samtasks.databinding.HomeFragmentBinding
import com.example.samtasks.ui.fragments.datepicker.DatePickerFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var binding: HomeFragmentBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(
            inflater,
            container,
            false
        )
        binding.greetingText.text =
            getString(homeViewModel.greetingTextResourceId, homeViewModel.user?.name ?: "")
        navController = findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = this@HomeFragment
            fragment = this@HomeFragment
        }
    }

    fun showDatePicker(){
        val pickerFragment = DatePickerFragment()
        // TODO : Handle the date properly
        pickerFragment.setCallback { date ->
            Timber.d(date)
        }
        pickerFragment.show(childFragmentManager,"datePicker")
    }

    fun openCreateTaskFragment(){
        navController.navigate(HomeFragmentDirections.actionHomeFragmentToCreateTaskFragment())
    }

}