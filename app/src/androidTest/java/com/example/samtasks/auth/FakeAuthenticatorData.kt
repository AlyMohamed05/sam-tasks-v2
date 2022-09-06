package com.example.samtasks.auth

object FakeAuthenticatorData {

    val fakeUser = User(
        "TEST User",
        "test@gmail.com",
        null,
        false
    )

    val registeredEmails = mutableListOf("test@gmail.com")
    val registeredPasswords = mutableListOf("testpassword")
}