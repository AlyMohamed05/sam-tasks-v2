package com.example.samtasks.ui.fragments.create_edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.samtasks.data.db.TasksDao
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    private val tasksDao: TasksDao
) : ViewModel() {

    val title = MutableLiveData("")
    val content = MutableLiveData("")

    private val _taskLocation = MutableLiveData<LatLng?>()
    val taskLocation: LiveData<LatLng?>
        get() = _taskLocation

    fun createTask(){
        Timber.d("Task Title : ${title.value.toString()}")
        Timber.d("Task Content : ${content.value.toString()}")
        // TODO : Need to be implemented !
    }

    fun setTaskLocation(location: LatLng){
        _taskLocation.value = location
    }
}