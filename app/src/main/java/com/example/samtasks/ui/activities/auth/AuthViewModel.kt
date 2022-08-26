package com.example.samtasks.ui.activities.auth

import androidx.lifecycle.ViewModel
import com.example.samtasks.auth.Authenticator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel  @Inject constructor(
    private val authenticator: Authenticator
): ViewModel(){

    val isSignedInStatus = authenticator.subscribeToSignInStatus()
}