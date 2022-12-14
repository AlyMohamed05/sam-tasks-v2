package com.udacity.project4.ui.fragments.create_edit

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.project4.data.models.Task
import com.udacity.project4.receivers.GeofenceBroadcastReceiver
import com.udacity.project4.utils.DispatchersProvider
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.udacity.project4.data.TasksDataSource
import kotlinx.coroutines.launch
import timber.log.Timber

@SuppressLint("UnspecifiedImmutableFlag")
class SaveReminderViewModel(
    context: Context,
    private val tasksRepository: TasksDataSource,
    private val dispatchersProvider: DispatchersProvider
) : ViewModel() {

    val title = MutableLiveData("")
    val content = MutableLiveData("")
    private var finished = false
    private var date: String? = null
    private var time: String? = null

    private val _taskLocation = MutableLiveData<LatLng?>(null)
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

    private val sharedPref: SharedPreferences
    private val geofenceIdKey = "GEOFENCE_ID"

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
            Timber.d("No mutability flag is set")
            PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

    init {
        geofencingClient = LocationServices.getGeofencingClient(context)
        sharedPref = context.getSharedPreferences(
            "sam_preferences",
            Context.MODE_PRIVATE
        )
    }

    fun createTask() {
        var geofenceId: String? = null
        // create a geofence if the location is set
        if (_taskLocation.value != null) {
            geofenceId = generateGeofenceId()
            createGeofence(geofenceId, _taskLocation.value!!)
        }
        val task = Task(
            title = title.value!!,
            content = content.value!!,
            finished = this@SaveReminderViewModel.finished,
            date = date,
            time = time,
            location = taskLocation.value,
            geofenceId = geofenceId
        )
        viewModelScope.launch(dispatchersProvider.main) {
            tasksRepository.newTask(task)
        }
        _jobFinished.value = true
    }

    fun setDate(date: String) {
        this@SaveReminderViewModel.date = date
    }

    fun setTime(time: String) {
        this@SaveReminderViewModel.time = time
    }

    fun setTaskLocation(location: LatLng) {
        _taskLocation.value = location
    }

    @SuppressLint("MissingPermission")
    private fun createGeofence(geofenceId: String, location: LatLng) {
        // TODO : find a proper way to generate id for the request.
        val geofence = Geofence.Builder()
            .setRequestId(geofenceId)
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

    /**
     * Generates an ID for the upcoming created geofence and uses shared pref
     * to keep track of next id.
     */
    private fun generateGeofenceId(): String {
        if (sharedPref.contains(geofenceIdKey)) {
            val id = sharedPref.getInt(geofenceIdKey, 0)
            sharedPref.edit().apply {
                putInt(geofenceIdKey, id + 1)
                apply()
                return id.toString()
            }
        } else {
            sharedPref.edit().apply {
                putInt(geofenceIdKey, 1)
                apply()
                return 0.toString()
            }
        }
    }
}