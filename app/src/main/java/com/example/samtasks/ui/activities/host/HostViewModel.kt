package com.example.samtasks.ui.activities.host

import androidx.lifecycle.ViewModel
import com.example.samtasks.auth.Authenticator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HostViewModel @Inject constructor(
    private val authenticator: Authenticator
) : ViewModel() {

    val isSignedIn = authenticator.isSignedIn
}