package com.example.samtasks.ui.fragments.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.samtasks.data.models.toEmail
import com.example.samtasks.data.models.toPassword
import com.example.samtasks.utils.ValidateResult
import timber.log.Timber

class LoginFormWithValidationImpl: LoginFormWithValidation {

    override val loginEmail: MutableLiveData<String> = MutableLiveData("")
    override val loginPassword: MutableLiveData<String> = MutableLiveData("")

    private val _loginEmailError = MutableLiveData(ValidateResult.VALID)
    override val loginEmailError: LiveData<ValidateResult>
        get() = _loginEmailError

    private val _loginPasswordError = MutableLiveData(ValidateResult.VALID)
    override val loginPasswordError: LiveData<ValidateResult>
        get() = _loginPasswordError

    override fun validateLoginCredentials(): Boolean {
        val email = loginEmail.value!!.toEmail()
        val password = loginPassword.value!!.toPassword()
        val emailValidation = email.validate()
        val passwordValidation = password.validate()
        _loginEmailError.value = emailValidation
        _loginPasswordError.value = passwordValidation
        if(emailValidation.isNotValid || passwordValidation.isNotValid){
            return false
        }
        return true
    }

}