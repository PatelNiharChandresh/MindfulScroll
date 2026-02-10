package com.rudy.mindfulscroll.data.local.entity

import com.rudy.mindfulscroll.domain.model.MonitoredApp
import com.rudy.mindfulscroll.domain.model.ThresholdConfig
import com.rudy.mindfulscroll.domain.model.UsageSession

fun MonitoredAppEntity.toDomain() = MonitoredApp(
    packageName = packageName,
    displayName = displayName,
    isActive = isActive,
    addedAt = addedAt,
)

fun MonitoredApp.toEntity() = MonitoredAppEntity(
    packageName = packageName,
    displayName = displayName,
    isActive = isActive,
    addedAt = addedAt,
)

fun ThresholdConfigEntity.toDomain() = ThresholdConfig(
    id = id,
    presetName = presetName,
    threshold1Min = threshold1Min,
    threshold2Min = threshold2Min,
    threshold3Min = threshold3Min,
    isGlobal = isGlobal,
    appPackageName = appPackageName,
)

fun ThresholdConfig.toEntity() = ThresholdConfigEntity(
    id = id,
    presetName = presetName,
    threshold1Min = threshold1Min,
    threshold2Min = threshold2Min,
    threshold3Min = threshold3Min,
    isGlobal = isGlobal,
    appPackageName = appPackageName,
)

fun UsageSessionEntity.toDomain() = UsageSession(
    id = id,
    appPackageName = appPackageName,
    startTimestamp = startTimestamp,
    endTimestamp = endTimestamp,
    durationSeconds = durationSeconds,
    maxThresholdReached = maxThresholdReached,
    throbActivated = throbActivated,
)

fun UsageSession.toEntity() = UsageSessionEntity(
    id = id,
    appPackageName = appPackageName,
    startTimestamp = startTimestamp,
    endTimestamp = endTimestamp,
    durationSeconds = durationSeconds,
    maxThresholdReached = maxThresholdReached,
    throbActivated = throbActivated,
)
