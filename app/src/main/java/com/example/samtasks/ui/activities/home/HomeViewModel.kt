package com.example.samtasks.ui.activities.home

import androidx.lifecycle.ViewModel
import com.example.samtasks.auth.Authenticator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authenticator: Authenticator
) : ViewModel(){

    var userIsSignedIn = authenticator.isSignedIn
}