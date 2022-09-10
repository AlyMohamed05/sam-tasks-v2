package com.udacity.project4.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.project4.data.TasksDataSource
import com.udacity.project4.data.models.Task

class FakeDataSource : TasksDataSource {

    private val tasksList = mutableListOf<Task>()

    private val _tasksListLive: MutableLiveData<List<Task>> = MutableLiveData(tasksList)
    private val tasksListLive: LiveData<List<Task>>
        get() = _tasksListLive

    override suspend fun newTask(task: Task) {
        tasksList.add(task)
        updateTasksLiveData()
    }

    override suspend fun updateTask(task: Task) {
        tasksList.replaceAll { currentTask ->
            if (currentTask.id == task.id)
                task
            else
                currentTask
        }
        if(task !in tasksList){
            tasksList.add(task)
        }
        updateTasksLiveData()
    }

    override suspend fun deleteTask(task: Task) {
        tasksList.remove(task)
        updateTasksLiveData()
    }

    override suspend fun getTasks(): List<Task> {
        return tasksList
    }

    override fun getTasksLive(): LiveData<List<Task>> {
        return tasksListLive
    }

    override suspend fun getTaskById(taskId: Int): Task? {
        return tasksList.find { task ->
            task.id == taskId
        }
    }

    override suspend fun getTaskByGeofenceId(geofenceId: String): Task? {
        return tasksList.find { task ->
            if (task.geofenceId == null) {
                return@find false
            }
            task.geofenceId == geofenceId
        }
    }

    private fun updateTasksLiveData() {
        _tasksListLive.value = tasksList
    }
}