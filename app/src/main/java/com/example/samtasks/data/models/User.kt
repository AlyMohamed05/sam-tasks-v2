package com.example.samtasks.data.models

import com.google.firebase.auth.FirebaseUser

data class User(
    val name: String?,
    val email: String?
)

fun FirebaseUser.toUser(): User {
    return User(
        name = displayName,
        email = email
    )
}