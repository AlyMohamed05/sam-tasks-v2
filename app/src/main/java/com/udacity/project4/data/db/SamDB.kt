package com.udacity.project4.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.udacity.project4.data.models.Task

@Database(
    entities = [Task::class],
    exportSchema = false,
    version = 1
)
@TypeConverters(Converters::class)
abstract class SamDB : RoomDatabase() {

    abstract val tasksDao: TasksDao
}