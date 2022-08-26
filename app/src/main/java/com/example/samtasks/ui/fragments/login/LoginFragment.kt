package com.example.samtasks.ui.fragments.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.samtasks.R
import com.example.samtasks.databinding.LoginFragmentBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModels()

    private lateinit var binding: LoginFragmentBinding
    private lateinit var navController: NavController
    private lateinit var googleSignInClient: GoogleSignInClient

    private val googleSignInHandler = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        if (task.isSuccessful) {
            try {
                loginViewModel.signInWithGoogleAccount(task.result)
            } catch (e: Exception) {
                Timber.d("Failed to get google account", e)
            }
        }
    }

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
            viewModel = loginViewModel
        }
        navController = findNavController()
        initClickListeners()
        initGoogleSignInClient()
    }

    private fun initClickListeners() {
        binding.apply {
            signupButton.setOnClickListener {
                navController.navigate(LoginFragmentDirections.actionLoginFragmentToSignupFragment())
            }
            googleButton.setOnClickListener { signInByGoogle() }
        }
    }

    private fun initGoogleSignInClient() {
        val signInOptions = GoogleSignInOptions.Builder()
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), signInOptions)
    }

    private fun signInByGoogle() {
        googleSignInHandler.launch(googleSignInClient.signInIntent)
    }
}