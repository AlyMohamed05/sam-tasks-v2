package com.example.samtasks.ui.fragments.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.samtasks.databinding.LoginFragmentBinding
import com.example.samtasks.ui.activities.auth.AuthViewModel
import com.example.samtasks.utils.errorMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: LoginFragmentBinding
    private lateinit var navController: NavController

    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoginFragmentBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = this@LoginFragment
            authViewModel = this@LoginFragment.authViewModel
        }
        navController = findNavController()
        initClickListeners()
        observe()
    }

    private fun initClickListeners() {
        binding.signUpTextButton.setOnClickListener {
            navController.navigate(LoginFragmentDirections.actionLoginFragmentToSignupFragment())
        }
    }

    private fun observe() {
        authViewModel.loginEmailError.observe(
            viewLifecycleOwner,
            Observer { binding.emailEdit.errorMessage(it) }
        )
        authViewModel.loginPasswordError.observe(
            viewLifecycleOwner,
            Observer { binding.passwordEdit.errorMessage(it) }
        )
    }
}