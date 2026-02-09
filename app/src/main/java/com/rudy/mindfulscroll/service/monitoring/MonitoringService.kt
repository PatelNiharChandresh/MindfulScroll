package com.rudy.mindfulscroll.service.monitoring

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * Stub for the foreground monitoring service.
 * Full implementation in Phase 3 (WBS 3.7).
 */
class MonitoringService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }
}
