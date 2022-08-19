package com.example.samtasks.ui.activities.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samtasks.auth.Authenticator
import com.example.samtasks.ui.fragments.login.LoginFormWithValidation
import com.example.samtasks.ui.fragments.login.LoginFormWithValidationImpl
import com.example.samtasks.ui.fragments.signup.SignUPFormWithValidationImpl
import com.example.samtasks.ui.fragments.signup.SignupFormWithValidation
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authenticator: Authenticator
) : ViewModel(),
    LoginFormWithValidation by LoginFormWithValidationImpl(),
    SignupFormWithValidation by SignUPFormWithValidationImpl() {

    private val _requestGoogleSignIn = MutableLiveData(false)
    val requestGoogleSignIn: LiveData<Boolean>
        get() = _requestGoogleSignIn

    fun login() {
        if (!validateLoginCredentials()) {
            return
        }
        viewModelScope.launch {
            val loginResponse = authenticator.login(loginEmail.value!!, loginPassword.value!!)
            // TODO : Handle auth response to see if there is error need to appear on InputLayout.
        }
    }

    fun continueWithGoogle() {
        _requestGoogleSignIn.value = true
    }

    /**
     * In Google sign in flow, first continueWithGoogle() function is called ,
     * which fires up a function in AuthActivity that gets google account, and
     * then the activity calls this function with the account as a parameter
     * to continue sign in with google flow.
     */
    fun signInWithGoogleAccount(account: GoogleSignInAccount){
        viewModelScope.launch {
            authenticator.loginWithCredentials(account)
        }
    }

    fun continueWithoutLogin() {

    }

    fun signUP() {
        if (!validateSignUPCredentials()) {
            return
        }
        viewModelScope.launch {
            val signupResponse = authenticator.createNewUser(
                signUpName.value!!,
                signUpEmail.value!!,
                signUpPassword.value!!
            )
            // TODO : Handle auth response to see if there is error need to appear on InputLayout.
        }
    }

    /**
     * Should be called by requestGoogleSignIn observers to reset and confirm
     * that request is being handled.
     */
    fun resetGoogleSignInRequest(){
        _requestGoogleSignIn.value = false
    }
}