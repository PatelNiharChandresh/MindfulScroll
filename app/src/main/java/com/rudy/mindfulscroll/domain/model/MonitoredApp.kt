package com.rudy.mindfulscroll.domain.model

data class MonitoredApp(
    val packageName: String,
    val displayName: String,
    val isActive: Boolean,
    val addedAt: Long,
)
