package com.udacity.project4.ui.activities.host

import androidx.lifecycle.ViewModel
import com.udacity.project4.auth.Authenticator

class HostViewModel(
    authenticator: Authenticator
) : ViewModel() {

    val isSignedIn = authenticator.isSignedIn
}