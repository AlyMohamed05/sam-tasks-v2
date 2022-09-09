package com.udacity.project4.data.repository

import androidx.lifecycle.LiveData
import com.udacity.project4.data.TasksDataSource
import com.udacity.project4.data.db.TasksDao
import com.udacity.project4.data.models.Task

class TasksLocalDataSource(
    private val tasksDao: TasksDao
) : TasksDataSource {

    override suspend fun newTask(task: Task) {
        return tasksDao.upsert(task)
    }

    override suspend fun updateTask(task: Task) {
        tasksDao.upsert(task)
    }

    override suspend fun deleteTask(task: Task) {
        tasksDao.delete(task)
    }

    override suspend fun getTasks(): List<Task> {
        return tasksDao.getTasks()
    }

    override fun getTasksLive(): LiveData<List<Task>> {
        return tasksDao.getTasksLive()
    }

    override suspend fun getTaskById(taskId: Int): Task? {
        return tasksDao.getTaskById(taskId)
    }

    override suspend fun getTaskByGeofenceId(geofenceId: String): Task? {
        return tasksDao.getTaskByGeofenceId(geofenceId)
    }

}