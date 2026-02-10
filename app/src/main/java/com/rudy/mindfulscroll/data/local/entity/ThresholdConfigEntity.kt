package com.rudy.mindfulscroll.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "threshold_configs")
data class ThresholdConfigEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val presetName: String?,
    val threshold1Min: Int,
    val threshold2Min: Int,
    val threshold3Min: Int,
    val isGlobal: Boolean = true,
    val appPackageName: String? = null,
)
