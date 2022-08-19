package com.example.samtasks.auth

/**
 * Contains results for login request and signup request.
 */
enum class AuthResult {
    SUCCESS,
    WRONG_CREDENTIALS,
    ACCOUNT_ALREADY_EXISTS,
    NETWORK_ERROR,
    UNKNOWN_ERROR
}