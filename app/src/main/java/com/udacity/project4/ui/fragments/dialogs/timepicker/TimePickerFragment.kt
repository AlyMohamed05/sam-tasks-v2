package com.udacity.project4.ui.fragments.dialogs.timepicker

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private var timeSetCallback: ((String) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(requireContext(), this, hour, minute, false)
    }

    override fun onTimeSet(picker: TimePicker?, hour: Int, minute: Int) {
        timeSetCallback?.let { callback ->
            val time = "$hour:$minute"
            callback(time)
        }
    }

    fun setCallback(callback: (String) -> Unit) {
        timeSetCallback = callback
    }
}