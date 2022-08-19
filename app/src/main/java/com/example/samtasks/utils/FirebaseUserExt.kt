package com.example.samtasks.utils

import com.example.samtasks.data.models.User
import com.google.firebase.auth.FirebaseUser


fun FirebaseUser.toUser(): User {
    return User(
        name = displayName,
        email = email
    )
}