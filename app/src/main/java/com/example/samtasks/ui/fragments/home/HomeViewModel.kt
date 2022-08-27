package com.example.samtasks.ui.fragments.home

import androidx.lifecycle.ViewModel
import com.example.samtasks.R
import com.example.samtasks.auth.Authenticator
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authenticator: Authenticator
) : ViewModel() {

    val user = authenticator.user

    val greetingTextResourceId: Int
        get() = getGreetingTextId()

    /**
     * Returns string resource for text shown at top.
     */
    private fun getGreetingTextId(): Int {
        val calendar = Calendar.getInstance()
        return when (calendar.get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> R.string.morning_greeting
            in 12..15 -> R.string.afternoon_greeting
            in 16..20 -> R.string.evening_greeting
            else -> R.string.night_greeting
        }
    }
}