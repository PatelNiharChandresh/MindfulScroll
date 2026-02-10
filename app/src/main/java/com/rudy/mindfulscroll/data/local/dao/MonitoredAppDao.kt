package com.rudy.mindfulscroll.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rudy.mindfulscroll.data.local.entity.MonitoredAppEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MonitoredAppDao {

    @Query("SELECT * FROM monitored_apps ORDER BY displayName COLLATE NOCASE")
    fun getAll(): Flow<List<MonitoredAppEntity>>

    @Query("SELECT * FROM monitored_apps WHERE isActive = 1")
    fun getActive(): Flow<List<MonitoredAppEntity>>

    @Query("SELECT packageName FROM monitored_apps WHERE isActive = 1")
    suspend fun getActivePackageNames(): List<String>

    @Upsert
    suspend fun upsert(app: MonitoredAppEntity)

    @Query("UPDATE monitored_apps SET isActive = :isActive WHERE packageName = :packageName")
    suspend fun setActive(packageName: String, isActive: Boolean)

    @Query("DELETE FROM monitored_apps WHERE packageName NOT IN (:installedPackages)")
    suspend fun removeUninstalled(installedPackages: List<String>)
}
