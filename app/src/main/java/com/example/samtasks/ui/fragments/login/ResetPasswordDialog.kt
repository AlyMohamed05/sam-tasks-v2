package com.example.samtasks.ui.fragments.login

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.example.samtasks.R
import timber.log.Timber
import java.lang.IllegalStateException

class ResetPasswordDialog : DialogFragment() {

    private var callback: ((String) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            buildDialog(it)
        } ?: throw IllegalStateException("Activity can't be null")
    }

    fun setCallback(callback: (String) -> Unit) {
        this@ResetPasswordDialog.callback = callback
    }

    private fun buildDialog(activity: FragmentActivity): Dialog {
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.reset_password_dialog_fragment, null)
        val builder = AlertDialog.Builder(activity)
            .setView(dialogView)
            .setPositiveButton(R.string.reset) { _, _ ->
                if (callback != null) {
                    val email = dialogView.findViewById<EditText>(R.id.email_edit).text.toString()
                    callback!!(email)
                } else {
                    Timber.d("YOU must assign a callback first\ncall setCallback()")
                }
            }
        return builder.create()
    }
}