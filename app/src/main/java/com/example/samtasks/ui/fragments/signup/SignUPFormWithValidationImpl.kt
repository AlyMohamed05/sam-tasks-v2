package com.example.samtasks.ui.fragments.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.samtasks.data.models.toEmail
import com.example.samtasks.data.models.toName
import com.example.samtasks.data.models.toPassword
import com.example.samtasks.utils.ValidateResult

class SignUPFormWithValidationImpl : SignupFormWithValidation {

    override val signUpName: MutableLiveData<String> = MutableLiveData("")
    override val signUpEmail: MutableLiveData<String> = MutableLiveData("")
    override val signUpPassword: MutableLiveData<String> = MutableLiveData("")

    private val _signUpNameError = MutableLiveData(ValidateResult.VALID)
    override val signUpNameError: LiveData<ValidateResult>
        get() = _signUpNameError

    private val _signUpEmailError = MutableLiveData(ValidateResult.VALID)
    override val signUpEmailError: LiveData<ValidateResult>
        get() = _signUpEmailError

    private val _signUpPasswordError = MutableLiveData(ValidateResult.VALID)
    override val signUpPasswordError: LiveData<ValidateResult>
        get() = _signUpPasswordError

    override fun validateSignUPCredentials(): Boolean {
        val name = signUpName.value!!.toName()
        val email = signUpEmail.value!!.toEmail()
        val password = signUpPassword.value!!.toPassword()
        val nameValidation = name.validate()
        val emailValidation = email.validate()
        val passwordValidation = password.validate()
        _signUpNameError.value = nameValidation
        _signUpEmailError.value = emailValidation
        _signUpPasswordError.value = passwordValidation
        if (nameValidation.isNotValid || emailValidation.isNotValid || passwordValidation.isNotValid) {
            return false
        }
        return true
    }
}