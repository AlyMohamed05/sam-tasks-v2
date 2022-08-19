package com.example.samtasks.ui.fragments.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.samtasks.databinding.SignupFragmentBinding
import com.example.samtasks.ui.activities.auth.AuthViewModel
import com.example.samtasks.utils.errorMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : Fragment() {

    private lateinit var binding: SignupFragmentBinding
    private lateinit var navController: NavController

    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SignupFragmentBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = this@SignupFragment
            authViewModel = this@SignupFragment.authViewModel
        }
        navController = findNavController()
        initClickListeners()
        observe()
    }

    private fun initClickListeners(){
        binding.backTextButton.setOnClickListener {
            navController.navigateUp()
        }
    }

    private fun observe() {
        authViewModel.signUpNameError.observe(
            viewLifecycleOwner,
            Observer { binding.nameEdit.errorMessage(it) }
        )
        authViewModel.signUpEmailError.observe(
            viewLifecycleOwner,
            Observer { binding.emailEdit.errorMessage(it) }
        )
        authViewModel.signUpPasswordError.observe(
            viewLifecycleOwner,
            Observer { binding.passwordEdit.errorMessage(it) }
        )
    }
}