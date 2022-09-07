package com.udacity.project4.adapters

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

/**
 * if resource id is  null then no error to show
 */
@BindingAdapter("errorValue")
fun TextInputLayout.errorValue(resourceId: Int?){
    if(resourceId == null){
        isErrorEnabled = false
        error = null
    }else{
        isErrorEnabled = true
        error = context.getString(resourceId)
    }
}