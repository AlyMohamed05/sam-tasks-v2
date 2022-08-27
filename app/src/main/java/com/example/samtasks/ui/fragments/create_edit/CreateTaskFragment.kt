package com.example.samtasks.ui.fragments.create_edit

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.samtasks.R
import com.example.samtasks.databinding.CreateTaskFragmentBinding
import com.example.samtasks.ui.fragments.datepicker.DatePickerFragment
import com.example.samtasks.ui.fragments.timepicker.TimePickerFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import timber.log.Timber

@AndroidEntryPoint
class CreateTaskFragment : Fragment() {

    private val createViewModel: CreateTaskViewModel by viewModels()

    private lateinit var binding: CreateTaskFragmentBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CreateTaskFragmentBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        binding.apply {
            lifecycleOwner = this@CreateTaskFragment
            viewModel = createViewModel
            fragment = this@CreateTaskFragment
        }
    }

    override fun onStart() {
        super.onStart()
        animateFabButtonOnStart()
    }

    fun showDatePicker() {
        val datePicker = DatePickerFragment()
        datePicker.setCallback { date ->
            Timber.d(date)
        }
        datePicker.show(childFragmentManager, "datePicker")
    }

    fun showMapPicker(){
        Toast.makeText(context,"Not Supported yet",Toast.LENGTH_LONG).show()
    }

    fun setAlarm(){
        Toast.makeText(context,"Not Supported yet",Toast.LENGTH_LONG).show()
    }

    fun showTimePicker() {
        val timePicker = TimePickerFragment()
        timePicker.setCallback { time ->
            Timber.d(time)
        }
        timePicker.show(childFragmentManager, "timePicker")
    }

    fun backWithoutSave() {
        navController.navigateUp()
    }

    private fun animateFabButtonOnStart() {
        lifecycleScope.launchWhenStarted {
            val enterAnimationDuration =
                resources.getInteger(R.integer.home_create_animations_duration).toLong()
            // Wait until the fragment is visible
            delay(enterAnimationDuration)
            val scaleButtonX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0f, 1f)
            val scaleButtonY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f, 1f)
            ObjectAnimator.ofPropertyValuesHolder(
                binding.createFab,
                scaleButtonX,
                scaleButtonY
            ).apply {
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator?) {
                        super.onAnimationStart(animation)
                        binding.createFab.visibility = View.VISIBLE
                    }
                })
                duration = enterAnimationDuration
                start()
            }
        }
    }
}