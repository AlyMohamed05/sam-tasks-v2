package com.example.samtasks.ui.activities.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.samtasks.R
import com.example.samtasks.ui.activities.home.HomeActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient

    private val authViewModel: AuthViewModel by viewModels()

    private val googleSignInHandler = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        if (task.isSuccessful) {
            authViewModel.signInWithGoogleAccount(task.result)
        } else {
            Toast.makeText(this, R.string.failed_login_with_google, Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        initGoogleSignInClient()
        observe()
    }

    private fun signInWithGoogle() {
        googleSignInHandler.launch(googleSignInClient.signInIntent)
    }

    private fun initGoogleSignInClient() {
        val googleSignInOptions = GoogleSignInOptions.Builder()
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
    }

    private fun observe() {
        authViewModel.requestGoogleSignIn
            .observe(this) { shouldSignInWithGoogle ->
                if (shouldSignInWithGoogle) {
                    authViewModel.resetGoogleSignInRequest()
                    signInWithGoogle()
                }
            }

        authViewModel.continueWithoutLogin.observe(this) { continueWithoutLogin ->
            if (continueWithoutLogin) {
                val pref = getSharedPreferences(getString(R.string.shared_pref_name), MODE_PRIVATE)
                pref.edit().run {
                    putBoolean(getString(R.string.continue_without_login), true)
                    apply()
                    Intent(this@AuthActivity, HomeActivity::class.java).also {
                        it.putExtra("no_splash",true)   // No Need to splash.
                        startActivity(it)
                        finish()
                    }
                }
            }
        }
        authViewModel.resetPassword
            .observe(this) { shouldRequestPasswordReset ->
                if (shouldRequestPasswordReset) {
                    authViewModel.resetPasswordResetRequest()
                    ResetPasswordFragment().apply {
                        setResetPasswordCallback { email ->
                            authViewModel.resetPassword(email)
                        }
                        show(supportFragmentManager, "ResetPasswordFragment")
                    }
                }
            }

        authViewModel.authEvents
            .observe(this) { event ->
                if (event != AuthEvents.EMPTY) {
                    authViewModel.clearAuthEvent()
                    showEventMessage(event)
                }
            }
    }

    private fun showEventMessage(event: AuthEvents) {
        val message: String? = when (event) {
            AuthEvents.SENT_RESET_PASSWORD_EMAIL -> getString(event.resourceCode)
            else -> null
        }
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}