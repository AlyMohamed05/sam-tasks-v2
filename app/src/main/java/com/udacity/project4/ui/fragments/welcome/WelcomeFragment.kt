package com.udacity.project4.ui.fragments.welcome

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.udacity.project4.R
import com.udacity.project4.databinding.WelcomeFragmentBinding
import timber.log.Timber

class WelcomeFragment : Fragment() {

    private lateinit var binding: WelcomeFragmentBinding

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { handleFirebaseSignInResult(it) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WelcomeFragmentBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListeners()
    }

    private fun createFirebaseSignInIntent(): Intent {
        val authProviders = listOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
        )
        return AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(authProviders)
            .setLogo(R.drawable.ic_todo_foreground)
            .setTheme(R.style.Theme_SAMTasks)
            .build()
    }

    private fun handleFirebaseSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            Timber.d("Logged in")
        } else {
            Timber.d("Handle error")
            Timber.d(response?.error?.errorCode.toString())
        }
    }

    /**
     * Saves user choice to shared preferences and navigates user to Home fragment.
     */
    private fun continueWithoutLogin() {
        activity?.let {
            it.getSharedPreferences(
                getString(R.string.shared_preferences_name),
                Context.MODE_PRIVATE
            ).edit().apply {
                putBoolean(getString(R.string.continue_without_login_key), true)
                apply()
            }
            findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToHomeFragment())
        }
    }

    private fun initClickListeners() {
        binding.apply {
            // Continue without login button
            continueTextButton.setOnClickListener {
                continueWithoutLogin()
            }
            loginButton.setOnClickListener {
                signInLauncher.launch(createFirebaseSignInIntent())
            }
        }
    }

}