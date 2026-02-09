package com.rudy.mindfulscroll.domain.model

data class UsageSession(
    val id: Long = 0,
    val appPackageName: String,
    val startTimestamp: Long,
    val endTimestamp: Long = 0,
    val durationSeconds: Int = 0,
    val maxThresholdReached: Int = 0,
    val throbActivated: Boolean = false,
)
