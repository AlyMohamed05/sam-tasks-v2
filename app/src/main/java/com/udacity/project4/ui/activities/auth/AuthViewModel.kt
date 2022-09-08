package com.udacity.project4.ui.activities.auth

import androidx.lifecycle.ViewModel
import com.udacity.project4.auth.Authenticator

class AuthViewModel(
    authenticator: Authenticator
) : ViewModel() {

    val isSignedInStatus = authenticator.subscribeToSignInStatus()
}