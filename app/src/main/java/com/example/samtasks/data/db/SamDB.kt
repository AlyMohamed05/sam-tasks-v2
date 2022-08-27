package com.example.samtasks.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.samtasks.data.models.Task

@Database(
    entities = [Task::class],
    exportSchema = false,
    version = 1
)
abstract class SamDB : RoomDatabase() {

    abstract val tasksDao: TasksDao
}