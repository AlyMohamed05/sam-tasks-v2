package com.example.samtasks.ui.fragments.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samtasks.R
import com.example.samtasks.auth.Authenticator
import com.example.samtasks.auth.LoginResponse
import com.example.samtasks.utils.DispatchersProvider
import com.example.samtasks.utils.ValidationResult
import com.example.samtasks.utils.validateAsEmail
import com.example.samtasks.utils.validateAsPassword
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authenticator: Authenticator,
    private val dispatchersProvider: DispatchersProvider
) : ViewModel() {

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
    val emailError = MutableLiveData<Int?>(null)
    val passwordError = MutableLiveData<Int?>(null)

    private val _uiEvents = MutableLiveData<UiEvents>(UiEvents.NoValue)
    val uiEvents: LiveData<UiEvents>
        get() = _uiEvents

    // Just to access values cleaner
    private val emailValue: String
        get() = email.value!!
    private val passwordValue: String
        get() = password.value!!

    fun login() {
        if (!validate()) {
            return
        }
        viewModelScope.launch(dispatchersProvider.main) {
            val response = authenticator.login(emailValue, passwordValue)
            handleLoginResponse(response)
        }
    }

    fun signInWithGoogleAccount(account: GoogleSignInAccount) {
        viewModelScope.launch(dispatchersProvider.main) {
            authenticator.signInWithGoogleAccount(
                account
            )
        }
    }

    fun sendResetPasswordEmail(email: String) {
        viewModelScope.launch(dispatchersProvider.main) {
            val succeeded = authenticator.requestPasswordReset(email)
            if (succeeded) {
                _uiEvents.value = UiEvents.ShowToast(R.string.sent)
            } else {
                _uiEvents.value = UiEvents.ShowToast(R.string.failed_to_reset)
            }
        }
    }

    fun resetUiEvents() {
        _uiEvents.value = UiEvents.NoValue
    }

    /**
     * LOCAL VALIDATION.
     * Validates email and password and returns true if valid and false if
     * no VALID.
     * This function will automatically set validation error messages.
     */
    private fun validate(): Boolean {
        val emailValidationResult = emailValue.validateAsEmail()
        val passwordValidationResult = passwordValue.validateAsPassword()
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
        if (emailValidationResult != ValidationResult.VALID || passwordValidationResult != ValidationResult.VALID) {
            return false
        }
        return true
    }

    private fun handleLoginResponse(response: LoginResponse) {
        when (response) {
            LoginResponse.SUCCEEDED -> {
                Timber.d("Logged in")
                // Clear error messages if it exists
                emailError.value = null
                passwordError.value = null
            }
            LoginResponse.WRONG_CREDENTIALS -> Timber.d("Wrong credentials")
            LoginResponse.WRONG_PASSWORD -> passwordError.value = R.string.wrong_password
            else -> Timber.d("Unexpected error occurred")
        }
    }
}

sealed class UiEvents(val resId: Int?) {
    class ShowToast(val stringId: Int) : UiEvents(stringId)
    object NoValue : UiEvents(null)
}