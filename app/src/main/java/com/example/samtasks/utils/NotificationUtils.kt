package com.example.samtasks.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.samtasks.R

private const val GEOFENCE_NOTIFICATION_ID = 0

fun NotificationManager.createChannel(
    channelId: String,
    channelName: String,
    channelDescription: String,
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = channelDescription
        createNotificationChannel(channel)
    }
}

fun NotificationManager.sendGeofenceNotification(
    context: Context,
    content: String
) {
    val channelId = context.getString(R.string.geofencing_notification_channel_id)
    val notification = NotificationCompat
        .Builder(context, channelId)
        .run {
            setSmallIcon(R.drawable.ic_todo_foreground)
            setContentTitle(context.getString(R.string.reminder))
            setContentText(content)
            priority = NotificationCompat.PRIORITY_MAX
            build()
        }
    notify(GEOFENCE_NOTIFICATION_ID, notification)
}