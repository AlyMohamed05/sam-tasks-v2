package com.example.samtasks.utils

import androidx.databinding.BindingAdapter
import com.example.samtasks.R
import com.google.android.material.textfield.TextInputLayout

interface Validatable {

    /**
     * This function should return a string that describes error if it EXISTS.
     * if there is no error return NULL.
     */
    fun validate(): ValidateResult
}

enum class ValidateResult(val resourceCode: Int) {

    VALID(Int.MIN_VALUE),
    EMPTY_FIELD(R.string.empty_field),
    EMAIL_NOT_VALID(R.string.email_not_valid),
    PASSWORD_TOO_SHORT(R.string.password_too_short);

    val isValid: Boolean
        get() = this==VALID

    val isNotValid: Boolean
        get() = !isValid
}

@BindingAdapter("errorMessage")
fun TextInputLayout.errorMessage(validationResult: ValidateResult){
    if(validationResult.isValid){
        isErrorEnabled = false
        error = null
    }else{
        isErrorEnabled = true
        error = context.getString(validationResult.resourceCode)
    }
}