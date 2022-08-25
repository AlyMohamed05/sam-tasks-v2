package com.example.samtasks.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.samtasks.data.models.Task

@Dao
interface TasksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(task: Task)

    @Query("SELECT * FROM tasks_table")
    suspend fun getTasks(): List<Task>

    @Query("SELECT * FROM tasks_table")
    fun getTasksLive(): LiveData<List<Task>>

    @Query("SELECT * FROM tasks_table WHERE date = :date")
    fun getTasksByDateLive(date: String): LiveData<List<Task>>

    @Delete
    suspend fun delete(task: Task)
}