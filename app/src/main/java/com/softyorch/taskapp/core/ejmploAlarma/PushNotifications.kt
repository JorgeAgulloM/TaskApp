/*
 * Copyright (c) 2022. File developed by Jorge Agulló Martín for SoftYorch
 */

package com.softyorch.taskapp.core.ejmploAlarma

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.softyorch.taskapp.ui.activities.MainActivity
import com.softyorch.taskapp.utils.sdk31AndUp

class PushNotifications(
    private val context: Context
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        createNotificationChannel()
        notifyNotification()
    }

    fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(
            MY_CHANNEL_ID,
            MY_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )

        NotificationManagerCompat.from(context).createNotificationChannel(notificationChannel)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun notifyNotification(textCompact:String = "Text Compact", bigText: String = "Description of task") {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val flag = if (sdk31AndUp) PendingIntent.FLAG_IMMUTABLE else 0
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, flag)

        val builder = NotificationCompat.Builder(context, MY_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_delete)
            .setContentTitle("TaskApp")
            .setContentText(textCompact)
            .setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(context)) {
            notify(1, builder.build())
        }

    }

    companion object {
        const val MY_CHANNEL_ID = "taskAppChannel"
        const val MY_CHANNEL_NAME = "taskApp"
    }
}



