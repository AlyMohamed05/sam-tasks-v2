package com.example.samtasks.auth

import androidx.lifecycle.LiveData
import com.example.samtasks.data.models.User
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface Authenticator {

    val user: User?
    val isSignedIn: Boolean

    suspend fun createNewUser(
        name: String,
        email: String,
        password: String
    ): AuthResult

    suspend fun login(
        email: String,
        password: String
    ): AuthResult

    suspend fun loginWithCredentials(account: GoogleSignInAccount): AuthResult

    /**
     * Returns livedata that returns boolean indicator whether the user
     * is signed in or NOT.
     */
    fun subscribeToSignInStatus(): LiveData<Boolean>
}