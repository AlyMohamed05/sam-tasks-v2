package com.example.samtasks.data.models

import android.util.Patterns
import com.example.samtasks.utils.Validatable
import com.example.samtasks.utils.ValidateResult
import timber.log.Timber

class Name(
    val name: String
) : Validatable {
    override fun validate(): ValidateResult {
        return if (name.isBlank()) {
            ValidateResult.EMPTY_FIELD
        } else {
            ValidateResult.VALID
        }
    }
}

fun String.toName(): Name {
    return Name(this)
}

class Email(
    val email: String
) : Validatable {
    override fun validate(): ValidateResult {
        return if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ValidateResult.EMAIL_NOT_VALID
        } else if (email.isBlank()) {
            ValidateResult.EMPTY_FIELD
        } else {
            ValidateResult.VALID
        }
    }
}

fun String.toEmail(): Email {
    return Email(this)
}

class Password(
    val password: String
) : Validatable {
    override fun validate(): ValidateResult {
        return if (password.isBlank()) {
            ValidateResult.EMPTY_FIELD
        } else if (password.length < 8) {
            return ValidateResult.PASSWORD_TOO_SHORT
        } else {
            ValidateResult.VALID
        }
    }
}

fun String.toPassword(): Password {
    return Password(this)
}