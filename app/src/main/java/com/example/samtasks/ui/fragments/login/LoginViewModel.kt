package com.example.samtasks.ui.fragments.login

import androidx.lifecycle.ViewModel
import com.example.samtasks.auth.Authenticator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authenticator: Authenticator
): ViewModel() {
}