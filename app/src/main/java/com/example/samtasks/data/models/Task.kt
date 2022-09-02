package com.example.samtasks.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

@Entity(tableName = "tasks_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val title: String = "",
    val content: String = "",
    val finished: Boolean = false,
    val date: String? = null,
    val time: String? = null,
    val location: LatLng? = null,
    val geofenceId: String? = null
)