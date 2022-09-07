package com.udacity.project4.utils

import android.util.Patterns

enum class ValidationResult {
    VALID,

    EMPTY,

    EMAIL_NOT_VALID,

    PASSWORD_TOO_SHORT
}

fun String.validateAsName(): ValidationResult {
    return if (this.isBlank()) {
        ValidationResult.EMPTY
    } else
        ValidationResult.VALID
}

fun String.validateAsEmail(): ValidationResult {
    return if (this.isBlank()) {
        ValidationResult.EMPTY
    } else if (!Patterns.EMAIL_ADDRESS.matcher(this).matches()) {
        ValidationResult.EMAIL_NOT_VALID
    } else {
        ValidationResult.VALID
    }
}

fun String.validateAsPassword(): ValidationResult {
    return if (this.isBlank()) {
        ValidationResult.EMPTY
    } else if (this.length < 8) {
        ValidationResult.PASSWORD_TOO_SHORT
    } else {
        ValidationResult.VALID
    }
}