package com.example.samtasks.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import timber.log.Timber

class GeofenceBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(p0: Context?, intent: Intent) {
        Timber.d("Received a broadcast")
        val geofenceEvent = GeofencingEvent.fromIntent(intent) ?: return
        if(geofenceEvent.hasError()) {
            val errorMessage = GeofenceStatusCodes.getStatusCodeString(
                geofenceEvent.errorCode
            )
            Timber.d(errorMessage)
            return
        }
    }
}