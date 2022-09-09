package com.udacity.project4.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.udacity.project4.R
import com.udacity.project4.ui.activities.host.HostActivity

private const val GEOFENCE_NOTIFICATION_ID = 0

private const val PENDING_INTENT_ID = 1000

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
    content: String,
    taskId: Int
) {
    val channelId = context.getString(R.string.geofencing_notification_channel_id)
    val intent = Intent(context, HostActivity::class.java)
    intent.putExtra("taskId",taskId)
    val pendingIntent = PendingIntent.getActivity(
        context,
        PENDING_INTENT_ID,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    val notification = NotificationCompat
        .Builder(context, channelId)
        .run {
            setSmallIcon(R.drawable.ic_todo_foreground)
            setContentTitle(context.getString(R.string.reminder))
            setContentText(content)
            setContentIntent(pendingIntent)
            setAutoCancel(true)
            priority = NotificationCompat.PRIORITY_MAX
            build()
        }
    notify(GEOFENCE_NOTIFICATION_ID, notification)
}