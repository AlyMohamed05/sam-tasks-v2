package com.udacity.project4.data

import androidx.lifecycle.LiveData
import com.udacity.project4.data.models.Task

interface TasksDataSource {

    suspend fun newTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun getTasks(): List<Task>
    fun getTasksLive(): LiveData<List<Task>>
    suspend fun getTaskByGeofenceId(geofenceId: String): Task?

}