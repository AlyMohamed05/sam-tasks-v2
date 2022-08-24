package com.example.samtasks.auth

import android.net.Uri
import com.google.firebase.auth.FirebaseUser

data class User(
    val name: String?,
    val email: String?,
    val photoURL: Uri?,
    val verified: Boolean
)

fun FirebaseUser.toUser() = User(
    name = this.displayName,
    email = this.email,
    photoURL = this.photoUrl,
    verified = this.isEmailVerified
)