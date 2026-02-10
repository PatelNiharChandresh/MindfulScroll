package com.rudy.mindfulscroll.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rudy.mindfulscroll.data.local.dao.MonitoredAppDao
import com.rudy.mindfulscroll.data.local.dao.ThresholdConfigDao
import com.rudy.mindfulscroll.data.local.dao.UsageSessionDao
import com.rudy.mindfulscroll.data.local.dao.UserPreferencesDao
import com.rudy.mindfulscroll.data.local.entity.MonitoredAppEntity
import com.rudy.mindfulscroll.data.local.entity.ThresholdConfigEntity
import com.rudy.mindfulscroll.data.local.entity.UsageSessionEntity
import com.rudy.mindfulscroll.data.local.entity.UserPreferenceEntity

@Database(
    entities = [
        MonitoredAppEntity::class,
        ThresholdConfigEntity::class,
        UsageSessionEntity::class,
        UserPreferenceEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class MindfulScrollDatabase : RoomDatabase() {
    abstract fun monitoredAppDao(): MonitoredAppDao
    abstract fun thresholdConfigDao(): ThresholdConfigDao
    abstract fun usageSessionDao(): UsageSessionDao
    abstract fun userPreferencesDao(): UserPreferencesDao
}
