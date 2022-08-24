package com.example.samtasks.auth

import androidx.lifecycle.LiveData
import com.example.samtasks.auth.LoginResponse.UNHANDLED_ERROR
import com.example.samtasks.auth.LoginResponse.UNKNOWN_ERROR

interface Authenticator {

    val user: User?
    val isSignedIn: Boolean

    suspend fun login(
        email: String,
        password: String
    ): LoginResponse

    suspend fun signup(
        name: String,
        email: String,
        password: String
    ): SignUpResponse

    /**
     * Returns true if the email is sent else FALSE.
     */
    suspend fun sendVerificationEmail(): Boolean

    /**
     * Returns true if the request is successful and email is sent,
     * else it will return FALSE.
     */
    suspend fun requestPasswordReset(email: String): Boolean

    /**
     * Sends a boolean indicating if user is signed in or not
     */
    fun subscribeToSignInStatus(): LiveData<Boolean>
}

/**
 * @property UNHANDLED_ERROR When Firebase throws unhandled FirebaseException.
 * @property UNKNOWN_ERROR when an Exception is thrown.
 * The previous errors will print a message if it exists to debug console.
 */
enum class LoginResponse {

    SUCCEEDED,
    WRONG_PASSWORD,
    WRONG_CREDENTIALS,
    NETWORK_ERROR,
    UNHANDLED_ERROR,
    UNKNOWN_ERROR
}

/**
 * @property UNHANDLED_ERROR When Firebase throws unhandled FirebaseException.
 * @property UNKNOWN_ERROR when an Exception is thrown.
 * The previous errors will print a message if it exists to debug console.
 */
enum class SignUpResponse{

    SUCCEEDED,
    USER_ALREADY_EXISTS,
    NETWORK_ERROR,
    UNHANDLED_ERROR,
    UNKNOWN_ERROR
}