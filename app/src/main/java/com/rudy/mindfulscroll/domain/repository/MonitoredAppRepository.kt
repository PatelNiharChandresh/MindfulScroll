package com.rudy.mindfulscroll.domain.repository

import com.rudy.mindfulscroll.domain.model.MonitoredApp
import kotlinx.coroutines.flow.Flow

interface MonitoredAppRepository {
    fun getAll(): Flow<List<MonitoredApp>>
    fun getActive(): Flow<List<MonitoredApp>>
    suspend fun getActivePackageNames(): Set<String>
    suspend fun upsert(app: MonitoredApp)
    suspend fun setActive(packageName: String, isActive: Boolean)
    suspend fun removeUninstalled(installedPackages: List<String>)
}
