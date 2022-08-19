package com.example.samtasks.ui.fragments.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.samtasks.utils.ValidateResult

interface SignupFormWithValidation {

    // Fields Used by TextInput to get current value
    val signUpName : MutableLiveData<String>
    val signUpEmail: MutableLiveData<String>
    val signUpPassword: MutableLiveData<String>

    // Fields Errors
    val signUpNameError: LiveData<ValidateResult>
    val signUpEmailError: LiveData<ValidateResult>
    val signUpPasswordError: LiveData<ValidateResult>

    /**
     * Will run validations and return true if VALID.
     * if there is validation errors it will return FALSE.
     */
    fun validateSignUPCredentials(): Boolean
}