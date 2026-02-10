package com.rudy.mindfulscroll.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "monitored_apps")
data class MonitoredAppEntity(
    @PrimaryKey
    val packageName: String,
    val displayName: String,
    val isActive: Boolean = true,
    val addedAt: Long,
)
