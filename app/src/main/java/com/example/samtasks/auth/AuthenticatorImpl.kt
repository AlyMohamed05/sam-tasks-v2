package com.example.samtasks.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.samtasks.data.models.User
import com.example.samtasks.utils.toUser
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.lang.Exception

class AuthenticatorImpl : Authenticator {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private var firebaseUser: FirebaseUser? = firebaseAuth.currentUser

    private val _isSignedInStatus = MutableLiveData(firebaseUser != null)
    private val signedInStatus: LiveData<Boolean>
        get() = _isSignedInStatus

    override val user: User?
        get() = firebaseUser?.toUser()
    override val isSignedIn: Boolean
        get() = firebaseUser != null

    override suspend fun createNewUser(name: String, email: String, password: String): AuthResult {
        return try {
            val response = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            firebaseUser = response.user
            if (firebaseUser != null) {
                _isSignedInStatus.value = isSignedIn
                AuthResult.SUCCESS
            } else {
                Timber.d("Unexpected error occurred")
                AuthResult.UNKNOWN_ERROR
            }
        } catch (firebaseException: FirebaseException) {
            when (firebaseException) {
                is FirebaseAuthUserCollisionException -> AuthResult.ACCOUNT_ALREADY_EXISTS
                is FirebaseNetworkException -> AuthResult.NETWORK_ERROR
                else -> {
                    Timber.d(firebaseException)
                    AuthResult.UNKNOWN_ERROR
                }
            }
        } catch (exception: Exception) {
            Timber.d(exception)
            AuthResult.UNKNOWN_ERROR
        }
    }

    override suspend fun login(email: String, password: String): AuthResult {
        return try {
            val response = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            firebaseUser = response.user
            if (firebaseUser != null) {
                _isSignedInStatus.value = isSignedIn
                AuthResult.SUCCESS
            } else {
                Timber.d("Unexpected error occurred")
                AuthResult.UNKNOWN_ERROR
            }
        } catch (firebaseException: FirebaseException) {
            when (firebaseException) {
                is FirebaseAuthInvalidUserException -> AuthResult.WRONG_CREDENTIALS
                is FirebaseAuthInvalidCredentialsException -> AuthResult.WRONG_CREDENTIALS
                is FirebaseNetworkException -> AuthResult.NETWORK_ERROR
                else -> {
                    Timber.d(firebaseException)
                    AuthResult.UNKNOWN_ERROR
                }
            }
        } catch (exception: Exception) {
            Timber.d(exception)
            AuthResult.UNKNOWN_ERROR
        }
    }

    override suspend fun loginWithCredentials(account: GoogleSignInAccount): AuthResult {
        return try {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            val response = firebaseAuth.signInWithCredential(credential).await()
            firebaseUser = response.user
            if (firebaseUser != null) {
                _isSignedInStatus.value = isSignedIn
                AuthResult.SUCCESS
            } else {
                AuthResult.UNKNOWN_ERROR
            }
        } catch (e: Exception) {
            Timber.d(e)
            AuthResult.UNKNOWN_ERROR
        }
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

    override fun subscribeToSignInStatus() = signedInStatus

}