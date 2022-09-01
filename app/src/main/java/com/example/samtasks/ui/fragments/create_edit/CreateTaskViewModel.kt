package com.example.samtasks.ui.fragments.create_edit

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samtasks.data.db.TasksDao
import com.example.samtasks.data.models.Task
import com.example.samtasks.receivers.GeofenceBroadcastReceiver
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    @ApplicationContext context: Context,
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

    // Geofencing
    private val geofencingClient: GeofencingClient
    private val geofencePendingIntent by lazy {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        if (Build.VERSION.SDK_INT >= 31) {
            PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

    init {
        geofencingClient = LocationServices.getGeofencingClient(context)
    }

    fun createTask() {
        val task = Task(
            title = title.value!!,
            content = content.value!!,
            finished = this@CreateTaskViewModel.finished,
            date = date,
            time = time,
            location = taskLocation.value
        )
        // create a geofence if the location is set
        if (_taskLocation.value != null) {
            createGeofence(_taskLocation.value!!)
        }
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

    @SuppressLint("MissingPermission")
    private fun createGeofence(location: LatLng) {
        // TODO : find a proper way to generate id for the request.
        val geofence = Geofence.Builder()
            .setRequestId("REQUEST_ID")
            .setCircularRegion(location.latitude, location.longitude, 100f)
            .setExpirationDuration(1000L * 60L * 720L)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
            .build()
        val request = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()
        geofencingClient.addGeofences(request, geofencePendingIntent).run {
            addOnSuccessListener { Timber.d("Created geofence") }
            addOnFailureListener { Timber.d("Failed to create geofence") }
        }
    }
}