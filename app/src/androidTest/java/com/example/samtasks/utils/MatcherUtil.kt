package com.example.samtasks.utils

import android.view.View
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.BaseMatcher
import org.hamcrest.Description

class HasHintError : BaseMatcher<View>() {

    private var message = "TextInputLayout That has a hint"

    override fun describeTo(description: Description?) {
        description?.appendText(message)
    }

    override fun matches(view: Any?): Boolean {
        val textInputLayout = view as TextInputLayout? ?: return false
        val errorText = textInputLayout.error ?: return false
        if (errorText.isBlank()) {
            return false
        }
        return true
    }

}