package com.example.samtasks.ui.fragments.home

import androidx.lifecycle.ViewModel
import com.example.samtasks.auth.Authenticator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    authenticator: Authenticator
) : ViewModel(){

    val user = authenticator.user

}