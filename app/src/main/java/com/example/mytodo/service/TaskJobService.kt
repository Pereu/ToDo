package com.example.mytodo.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.mytodo.R
import com.example.mytodo.tasks.TasksActivity


class TaskJobService : JobService() {

    var mNotifyManager: NotificationManager? = null

    override fun onStartJob(jobParameters: JobParameters?): Boolean {

        createNotificationChannel()

        val contentPendingIntent = PendingIntent.getActivity(
            this, 0, Intent(this, TasksActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
            .setContentTitle(jobParameters?.extras?.getString("title") ?: "title")
            .setContentText(jobParameters?.extras?.getString("description") ?: "description")
            .setContentIntent(contentPendingIntent)
            .setSmallIcon(R.drawable.ic_add)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)

        mNotifyManager?.notify(0, builder.build())

        return false
    }

    override fun onStopJob(jobParameters: JobParameters?): Boolean {
        return true
    }

    private fun createNotificationChannel() {
        mNotifyManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

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
            mNotifyManager?.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        private const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
    }
}