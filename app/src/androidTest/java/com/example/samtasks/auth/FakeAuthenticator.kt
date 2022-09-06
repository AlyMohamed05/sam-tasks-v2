package com.example.samtasks.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlin.random.Random

/**
 * No user by default unless you login with ("test@gmail.com","testpassword") or you signup with any credentials.
 */
class FakeAuthenticator : Authenticator {

    private var currentUser: User? = null
        set(value) {
            field = value
            _isSignedInStatus.value = true
        }

    private val _isSignedInStatus = MutableLiveData(false)
    private val isSignedInStatus: LiveData<Boolean>
        get() = _isSignedInStatus

    override val user: User?
        get() = currentUser

    override val isSignedIn: Boolean
        get() = currentUser != null

    override suspend fun login(email: String, password: String): LoginResponse {
        return if (
            email in FakeAuthenticatorData.registeredEmails &&
            password in FakeAuthenticatorData.registeredPasswords
        ) {
            currentUser = FakeAuthenticatorData.fakeUser
            LoginResponse.SUCCEEDED
        } else {
            LoginResponse.WRONG_CREDENTIALS
        }
    }

    override suspend fun signup(name: String, email: String, password: String): SignUpResponse {
        FakeAuthenticatorData.registeredEmails.add(email)
        FakeAuthenticatorData.registeredPasswords.add(password)
        currentUser = FakeAuthenticatorData.fakeUser
        return SignUpResponse.SUCCEEDED
    }

    override suspend fun signInWithGoogleAccount(account: GoogleSignInAccount) {
        currentUser = FakeAuthenticatorData.fakeUser
    }

    override suspend fun sendVerificationEmail(): Boolean {
        return Random.nextBoolean()
    }

    override suspend fun requestPasswordReset(email: String): Boolean {
        return Random.nextBoolean()
    }

    override fun subscribeToSignInStatus(): LiveData<Boolean> {
        return isSignedInStatus
    }
}