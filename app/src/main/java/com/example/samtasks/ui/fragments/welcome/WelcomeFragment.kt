package com.example.samtasks.ui.fragments.welcome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.samtasks.R
import com.example.samtasks.databinding.WelcomeFragmentBinding
import com.example.samtasks.ui.activities.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment : Fragment() {

    private lateinit var binding: WelcomeFragmentBinding

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

    private fun initClickListeners() {
        binding.apply {
            // Continue without login button
            continueTextButton.setOnClickListener {
                continueWithoutLogin()
            }
            loginButton.setOnClickListener {
                Intent(activity, AuthActivity::class.java).apply {
                    startActivity(this)
                    activity?.finish()
                }
            }
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
            // TODO : Navigate to home fragment
        }
    }

}