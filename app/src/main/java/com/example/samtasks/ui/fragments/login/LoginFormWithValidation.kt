package com.example.samtasks.ui.fragments.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.samtasks.utils.ValidateResult

interface LoginFormWithValidation {

    // Fields Used by TextInput to get current value
    val loginEmail: MutableLiveData<String>
    val loginPassword: MutableLiveData<String>

    // Fields Errors
    val loginEmailError: LiveData<ValidateResult>
    val loginPasswordError: LiveData<ValidateResult>

    /**
     * Will run validations and return true if VALID.
     * if there is validation errors it will return FALSE.
     */
    fun validateLoginCredentials(): Boolean
}