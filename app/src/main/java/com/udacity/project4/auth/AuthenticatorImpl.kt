package com.udacity.project4.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.lang.IllegalStateException

class AuthenticatorImpl : Authenticator {

    // This LiveData is updated by the setter of firebaseUser
    private val _isSignedInStatus = MutableLiveData(false)

    private val firebaseAuth = FirebaseAuth.getInstance()

    private var firebaseUser = firebaseAuth.currentUser
        set(value) {
            field = value
            _isSignedInStatus.value = firebaseUser != null
        }

    override val user: User?
        get() = firebaseUser?.toUser()

    override val isSignedIn: Boolean
        get() = firebaseUser != null

    override suspend fun login(email: String, password: String): LoginResponse {
        return try {
            val response = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            firebaseUser = response.user
            if (firebaseUser == null) LoginResponse.UNHANDLED_ERROR else LoginResponse.SUCCEEDED
        } catch (firebaseException: FirebaseException) {
            when (firebaseException) {
                is FirebaseAuthInvalidUserException -> LoginResponse.WRONG_CREDENTIALS
                is FirebaseAuthInvalidCredentialsException -> LoginResponse.WRONG_PASSWORD
                is FirebaseNetworkException -> LoginResponse.NETWORK_ERROR
                else -> {
                    Timber.d(firebaseException)
                    LoginResponse.UNHANDLED_ERROR
                }
            }
        } catch (exception: Exception) {
            Timber.d(exception)
            LoginResponse.UNKNOWN_ERROR
        }
    }

    override suspend fun signInWithGoogleAccount(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        val response = firebaseAuth.signInWithCredential(credential).await()
        firebaseUser = response.user
        if (firebaseUser == null) {
            Timber.d("Failed to login with google")
        }
    }

    override suspend fun signup(name: String, email: String, password: String): SignUpResponse {
        return try {
            val response = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            firebaseUser = response.user
            if (firebaseUser == null) {
                throw IllegalStateException("Firebase user can't be null after signup")
            }
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
            firebaseUser!!.updateProfile(profileUpdates).await()
            SignUpResponse.SUCCEEDED
        } catch (firebaseException: FirebaseException) {
            when (firebaseException) {
                is FirebaseAuthUserCollisionException -> SignUpResponse.USER_ALREADY_EXISTS
                is FirebaseNetworkException -> SignUpResponse.NETWORK_ERROR
                else -> {
                    Timber.d(firebaseException)
                    SignUpResponse.UNHANDLED_ERROR
                }
            }
        } catch (exception: Exception) {
            Timber.d(exception)
            SignUpResponse.UNKNOWN_ERROR
        }
    }

    override suspend fun sendVerificationEmail(): Boolean {
        return if (firebaseUser == null) {
            false
        } else {
            try {
                firebaseUser!!.sendEmailVerification().await()
                true
            } catch (exception: Exception) {
                Timber.d(exception)
                false
            }
        }
    }

    override suspend fun requestPasswordReset(email: String): Boolean {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            true
        } catch (exception: Exception) {
            Timber.d(exception)
            false
        }
    }

    override fun subscribeToSignInStatus(): LiveData<Boolean> {
        return _isSignedInStatus
    }
}