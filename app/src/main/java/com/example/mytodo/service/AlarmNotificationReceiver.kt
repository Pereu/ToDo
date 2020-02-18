package com.example.mytodo.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.mytodo.R
import com.example.mytodo.tasks.TasksActivity


class AlarmNotificationReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {

        val notificationManager = createNotificationChannel(context)

        val contentPendingIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, TasksActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context!!, PRIMARY_CHANNEL_ID)
            .setContentTitle(intent?.getStringExtra("title") ?: "title")
            .setContentText(intent?.getStringExtra("description") ?: "description")
            .setContentIntent(contentPendingIntent)
            .setSmallIcon(R.drawable.ic_add)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)

        notificationManager.notify(0, builder.build())


    }

    private fun createNotificationChannel(context: Context?): NotificationManager {
        val mNotifyManager =
            context?.getSystemService(JobService.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                PRIMARY_CHANNEL_ID,
                "Job Service notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Notifications from Job Service"
            mNotifyManager.createNotificationChannel(notificationChannel)
        }

        return mNotifyManager
    }

    companion object {
        private const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
    }
}