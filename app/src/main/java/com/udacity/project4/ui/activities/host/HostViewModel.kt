package com.udacity.project4.ui.activities.host

import androidx.lifecycle.ViewModel
import com.udacity.project4.auth.Authenticator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HostViewModel @Inject constructor(
    private val authenticator: Authenticator
) : ViewModel() {

    val isSignedIn = authenticator.isSignedIn
}