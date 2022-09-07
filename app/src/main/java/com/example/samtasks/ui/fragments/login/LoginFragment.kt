package com.example.samtasks.ui.fragments.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
        initClickListeners()
        initGoogleSignInClient()
        observe()
    }

    /**
     * setup observers for events from login view model.
     */
    private fun observe() {
        loginViewModel.uiEvents.observe(viewLifecycleOwner) { event ->
            when (event) {
                is UiEvents.ShowToast -> {
                    loginViewModel.resetUiEvents()
                    Toast.makeText(context, event.stringId, Toast.LENGTH_LONG).show()
                }
                UiEvents.NoValue -> {}
            }
        }
    }

    private fun initClickListeners() {
        binding.apply {
            signupButton.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignupFragment())
            }
            googleButton.setOnClickListener { signInByGoogle() }
            forgotPasswordTextButton.setOnClickListener { showResetPasswordDialog() }
        }
    }

    private fun showResetPasswordDialog() {
        val resetPasswordDialog = ResetPasswordDialog()
        resetPasswordDialog.setCallback { email ->
            loginViewModel.sendResetPasswordEmail(email)
        }
        resetPasswordDialog.show(childFragmentManager, "Reset Password")
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