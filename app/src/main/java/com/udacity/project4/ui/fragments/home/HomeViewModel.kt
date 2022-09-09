package com.udacity.project4.ui.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.project4.R
import com.udacity.project4.auth.Authenticator
import com.udacity.project4.data.TasksDataSource
import com.udacity.project4.data.models.Task
import kotlinx.coroutines.launch
import java.util.*

class HomeViewModel(
    authenticator: Authenticator,
    private val tasksRepository: TasksDataSource
) : ViewModel() {

    val user = authenticator.user

    val greetingTextResourceId: Int
        get() = getGreetingTextId()

    val currentTasksList: LiveData<List<Task>> = tasksRepository.getTasksLive()

    private val _taskFromIntent: MutableLiveData<Task?> = MutableLiveData(null)
    val taskFromIntent: LiveData<Task?>
        get() = _taskFromIntent


    fun showTask(taskId: Int) {
        viewModelScope.launch {
            val task = tasksRepository.getTaskById(taskId)
            _taskFromIntent.value = task
        }
    }

    fun resetTaskFromIntent() {
        _taskFromIntent.value = null
    }

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