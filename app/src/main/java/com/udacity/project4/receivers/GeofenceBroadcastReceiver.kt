package com.udacity.project4.receivers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.udacity.project4.data.db.TasksDao
import com.udacity.project4.utils.sendGeofenceNotification
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

class GeofenceBroadcastReceiver : BroadcastReceiver(), KoinComponent {

    private val broadcastScope = CoroutineScope(Dispatchers.IO)

    val tasksDao: TasksDao by inject()

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
            val task = tasksDao.getTaskByGeofenceId(geofenceId)
            withContext(Dispatchers.Main) {
                notificationManager.sendGeofenceNotification(
                    context,
                    task?.title ?: ""
                )
            }

        }
    }
}
