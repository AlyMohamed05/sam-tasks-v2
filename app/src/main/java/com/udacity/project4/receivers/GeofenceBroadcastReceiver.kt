package com.udacity.project4.receivers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.udacity.project4.data.db.TasksDao
import com.udacity.project4.utils.sendGeofenceNotification
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class GeofenceBroadcastReceiver : BroadcastReceiver() {

    private val broadcastScope = CoroutineScope(Dispatchers.IO)

    @Inject
    lateinit var tasksDao: TasksDao

    override fun onReceive(context: Context, intent: Intent) {
        Timber.d("Received a broadcast")
        val geofenceEvent = GeofencingEvent.fromIntent(intent) ?: return
        if (geofenceEvent.hasError()) {
            val errorMessage = GeofenceStatusCodes.getStatusCodeString(
                geofenceEvent.errorCode
            )
            Timber.d(errorMessage)
            return
        }
        val triggeredGeofence = geofenceEvent.triggeringGeofences ?: return
        if (triggeredGeofence.isEmpty()) {
            return
        }
        Timber.d("Count of triggered geofence : ${triggeredGeofence.size}")
        val geofenceId = triggeredGeofence[0].requestId
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        broadcastScope.launch {
            if (::tasksDao.isInitialized) {
                val task = tasksDao.getTaskByGeofenceId(geofenceId)
                withContext(Dispatchers.Main) {
                    notificationManager.sendGeofenceNotification(
                        context,
                        task?.title ?: ""
                    )
                }
            } else {
                Timber.d("tasksDao is not initialized")
            }
        }
    }
}