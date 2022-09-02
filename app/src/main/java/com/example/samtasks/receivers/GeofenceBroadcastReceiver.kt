package com.example.samtasks.receivers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.samtasks.utils.sendGeofenceNotification
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import timber.log.Timber

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Timber.d("Received a broadcast")
        val geofenceEvent = GeofencingEvent.fromIntent(intent)
        if(geofenceEvent==null){
            Timber.d("Geofence event is null")
            return
        }
        if (geofenceEvent.hasError()) {
            val errorMessage = GeofenceStatusCodes.getStatusCodeString(
                geofenceEvent.errorCode
            )
            Timber.d(errorMessage)
            return
        }
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.sendGeofenceNotification(
            context,
            "Test for the geofencing notification"
        )
    }
}