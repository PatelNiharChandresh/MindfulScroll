package com.rudy.mindfulscroll

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.rudy.mindfulscroll.util.constants.AppConstants
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MindfulScrollApp : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            AppConstants.NOTIFICATION_CHANNEL_ID,
            AppConstants.NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Notification for active monitoring service"
            setShowBadge(false)
            enableVibration(false)
            setSound(null, null)
        }

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}
