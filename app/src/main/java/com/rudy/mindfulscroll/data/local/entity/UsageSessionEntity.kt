package com.rudy.mindfulscroll.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "usage_sessions",
    indices = [
        Index("appPackageName"),
        Index("startTimestamp"),
    ]
)
data class UsageSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val appPackageName: String,
    val startTimestamp: Long,
    val endTimestamp: Long,
    val durationSeconds: Int,
    val maxThresholdReached: Int,
    val throbActivated: Boolean = false,
)
