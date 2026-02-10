package com.rudy.mindfulscroll.data.repository

import com.rudy.mindfulscroll.data.local.dao.MonitoredAppDao
import com.rudy.mindfulscroll.data.local.entity.toDomain
import com.rudy.mindfulscroll.data.local.entity.toEntity
import com.rudy.mindfulscroll.domain.model.MonitoredApp
import com.rudy.mindfulscroll.domain.repository.MonitoredAppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MonitoredAppRepositoryImpl @Inject constructor(
    private val dao: MonitoredAppDao,
) : MonitoredAppRepository {

    override fun getAll(): Flow<List<MonitoredApp>> =
        dao.getAll().map { list -> list.map { it.toDomain() } }

    override fun getActive(): Flow<List<MonitoredApp>> =
        dao.getActive().map { list -> list.map { it.toDomain() } }

    override suspend fun getActivePackageNames(): Set<String> =
        withContext(Dispatchers.IO) {
            dao.getActivePackageNames().toSet()
        }

    override suspend fun upsert(app: MonitoredApp) =
        withContext(Dispatchers.IO) {
            dao.upsert(app.toEntity())
        }

    override suspend fun setActive(packageName: String, isActive: Boolean) =
        withContext(Dispatchers.IO) {
            dao.setActive(packageName, isActive)
        }

    override suspend fun removeUninstalled(installedPackages: List<String>) =
        withContext(Dispatchers.IO) {
            dao.removeUninstalled(installedPackages)
        }
}
