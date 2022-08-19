package com.example.samtasks.ui.activities.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.samtasks.R
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
        authViewModel.requestGoogleSignIn.observe(
            this,
            Observer { shouldSignInWithGoogle ->
                if (shouldSignInWithGoogle) {
                    authViewModel.resetGoogleSignInRequest()
                    signInWithGoogle()
                }
            }
        )
    }
}