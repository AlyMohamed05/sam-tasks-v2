package com.example.samtasks.ui.fragments.create_edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samtasks.data.db.TasksDao
import com.example.samtasks.data.models.Task
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    private val tasksDao: TasksDao
) : ViewModel() {

    val title = MutableLiveData("")
    val content = MutableLiveData("")
    private var finished = false
    private var date: String? = null
    private var time: String? = null

    private val _taskLocation = MutableLiveData<LatLng?>()
    val taskLocation: LiveData<LatLng?>
        get() = _taskLocation

    val isDateOrTimeSet: Boolean
        get() = date != null || time != null
    val isLocationSet: Boolean
        get() = _taskLocation.value != null

    // Indicates if task is created and task screen should close
    private val _jobFinished = MutableLiveData(false)
    val jobFinished: LiveData<Boolean>
        get() = _jobFinished

    fun createTask() {
        val task = Task(
            title = title.value!!,
            content = content.value!!,
            finished = this@CreateTaskViewModel.finished,
            date = date,
            time = time,
            location = taskLocation.value
        )
        viewModelScope.launch {
            tasksDao.upsert(task)
        }
        _jobFinished.value = true
    }

    fun setDate(date: String) {
        this@CreateTaskViewModel.date = date
    }

    fun setTime(time: String) {
        this@CreateTaskViewModel.time = time
    }

    fun setTaskLocation(location: LatLng) {
        _taskLocation.value = location
    }
}