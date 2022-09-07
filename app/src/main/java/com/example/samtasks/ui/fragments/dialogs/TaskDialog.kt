package com.example.samtasks.ui.fragments.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.example.samtasks.R
import com.example.samtasks.data.models.Task
import java.lang.IllegalStateException

class TaskDialog(
    private val task: Task
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        activity?.let { return createDialog(it) } ?: throw IllegalStateException()
    }

    private fun createDialog(activity: FragmentActivity): Dialog {
        val builder = AlertDialog.Builder(activity)
        val taskView = activity.layoutInflater.inflate(R.layout.task_dialog, null)
        taskView.findViewById<TextView>(R.id.title_text).text = task.title
        taskView.findViewById<TextView>(R.id.content_text).text = task.content
        builder.setView(taskView)
        builder.setPositiveButton(android.R.string.ok, null)
        return builder.create()
    }
}