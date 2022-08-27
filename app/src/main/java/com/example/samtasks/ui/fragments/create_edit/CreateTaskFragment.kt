package com.example.samtasks.ui.fragments.create_edit

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.samtasks.R
import com.example.samtasks.databinding.CreateTaskFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class CreateTaskFragment : Fragment() {

    private val createViewModel: CreateTaskViewModel by viewModels()

    private lateinit var binding: CreateTaskFragmentBinding

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

    override fun onStart() {
        super.onStart()
        animateFabButtonOnStart()
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