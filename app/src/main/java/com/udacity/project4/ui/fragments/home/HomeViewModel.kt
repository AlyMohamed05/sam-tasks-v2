package com.udacity.project4.ui.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.udacity.project4.R
import com.udacity.project4.auth.Authenticator
import com.udacity.project4.data.db.TasksDao
import com.udacity.project4.data.models.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    authenticator: Authenticator,
    tasksDao: TasksDao
) : ViewModel() {

    val user = authenticator.user

    val greetingTextResourceId: Int
        get() = getGreetingTextId()

    val currentTasksList: LiveData<List<Task>> = tasksDao.getTasksLive()

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