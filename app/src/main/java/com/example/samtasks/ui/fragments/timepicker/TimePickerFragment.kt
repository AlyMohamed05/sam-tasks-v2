package com.example.samtasks.ui.fragments.timepicker

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.text.DateFormat
import java.util.*

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private var onTimeSetCallback: ((String) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(requireContext(), this, hour, minute, false)
    }

    override fun onTimeSet(picker: TimePicker?, hour: Int, minute: Int) {
        onTimeSetCallback?.let { callback ->
            // TODO : Parse the time and pass it to the callback
            callback("TIME NOT PARSED YET")
        }
    }

    fun setCallback(callback: (String) -> Unit) {
        onTimeSetCallback = callback
    }
}