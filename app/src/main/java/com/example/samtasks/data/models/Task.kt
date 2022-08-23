package com.example.samtasks.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val title: String,
    val content: String,
    val time: String,
    val finished: Boolean = false
)
