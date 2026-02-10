package com.rudy.mindfulscroll

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.rudy.mindfulscroll.domain.repository.UsageSessionRepository
import com.rudy.mindfulscroll.util.constants.AppConstants
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class MindfulScrollApp : Application() {

    @Inject
    lateinit var usageSessionRepository: UsageSessionRepository

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        cleanupOldSessions()
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

    private fun cleanupOldSessions() {
        applicationScope.launch {
            val cutoff = System.currentTimeMillis() -
                (AppConstants.SESSION_RETENTION_DAYS * 24 * 60 * 60 * 1000L)
            usageSessionRepository.deleteOlderThan(cutoff)
        }
    }
}
