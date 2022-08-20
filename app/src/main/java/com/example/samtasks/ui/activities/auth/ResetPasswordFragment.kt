package com.example.samtasks.ui.activities.auth

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.findFragment
import com.example.samtasks.R
import com.example.samtasks.data.models.toEmail
import com.example.samtasks.utils.errorMessage
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import timber.log.Timber
import java.lang.IllegalStateException

class ResetPasswordFragment : DialogFragment() {

    private var callback: ((String) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            buildResetPasswordDialog(it)
        } ?: throw IllegalStateException("Activity can't be null")
    }

    /**
     * You must call this method to init the CALLBACK.
     */
    fun setResetPasswordCallback(callback: (String) -> Unit) {
        this@ResetPasswordFragment.callback = callback
    }

    private fun buildResetPasswordDialog(fragmentActivity: FragmentActivity): Dialog {
        return with(fragmentActivity) {
            val dialogBuilder = AlertDialog.Builder(this)
            val content = layoutInflater.inflate(R.layout.forgot_password_dialog, null)
            dialogBuilder.setView(content)
                .setPositiveButton(
                    R.string.reset_password
                ) { _, _ ->
                    val emailText = content.findViewById<TextInputEditText>(R.id.reset_email_edit)
                        .text.toString()
                    callback?.let {
                        it(emailText)
                    }
                }
            dialogBuilder.create()
        }
    }

}