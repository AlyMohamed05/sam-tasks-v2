package com.udacity.project4.ui.fragments.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.project4.R
import com.udacity.project4.auth.Authenticator
import com.udacity.project4.auth.SignUpResponse
import com.udacity.project4.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val authenticator: Authenticator,
    private val dispatchersProvider: DispatchersProvider
) : ViewModel() {

    val name = MutableLiveData("")
    val email = MutableLiveData("")
    val password = MutableLiveData("")

    /**
     * Error messages that appears on input layout.
     * They might come from local validation or errors coming from
     * authentication service.
     *  <Important> You need to pass a string resource id indicating the
     *  error message to appear to user on input layout or pass null if
     *  there is no error.
     */
    val nameError = MutableLiveData<Int?>(null)
    val emailError = MutableLiveData<Int?>(null)
    val passwordError = MutableLiveData<Int?>(null)

    private val nameValue: String
        get() = name.value!!
    private val emailValue: String
        get() = email.value!!
    private val passwordValue: String
        get() = password.value!!

    fun signup() {
        if (!validate()) {
            return
        }
        viewModelScope.launch(dispatchersProvider.main) {
            val response = authenticator.signup(nameValue, emailValue, passwordValue)
            handleSignupResponse(response)
        }
    }

    /**
     * LOCAL VALIDATION.
     * Validates email and password and returns true if valid and false if
     * no VALID.
     * This function will automatically set validation error messages.
     */
    private fun validate(): Boolean {
        val nameValidationResult = nameValue.validateAsName()
        val emailValidationResult = emailValue.validateAsEmail()
        val passwordValidationResult = passwordValue.validateAsPassword()
        when (nameValidationResult) {
            ValidationResult.EMPTY -> nameError.value = R.string.empty_field
            ValidationResult.VALID -> nameError.value = null
            else -> {
                Timber.d("validate() unhandled situation")
            }
        }
        when (emailValidationResult) {
            ValidationResult.EMPTY -> emailError.value = R.string.empty_field
            ValidationResult.EMAIL_NOT_VALID -> emailError.value = R.string.email_not_valid
            ValidationResult.VALID -> emailError.value = null
            else -> {
                Timber.d("validate() unhandled situation")
            }
        }
        when (passwordValidationResult) {
            ValidationResult.EMPTY -> passwordError.value = R.string.empty_field
            ValidationResult.PASSWORD_TOO_SHORT -> passwordError.value = R.string.short_password
            ValidationResult.VALID -> passwordError.value = null
            else -> {
                Timber.d("validate() unhandled situation")
            }
        }
        if (nameValidationResult != ValidationResult.VALID ||
            emailValidationResult != ValidationResult.VALID ||
            passwordValidationResult != ValidationResult.VALID
        ) {
            return false
        }
        return true
    }

    private fun handleSignupResponse(response: SignUpResponse) {
        // TODO : Handle the rest of the situations.
        when (response) {
            SignUpResponse.SUCCEEDED -> Timber.d("Signed up successfully")
            SignUpResponse.USER_ALREADY_EXISTS -> emailError.value = R.string.email_exists
            else -> Timber.d("Unhandled value yet")
        }
    }
}