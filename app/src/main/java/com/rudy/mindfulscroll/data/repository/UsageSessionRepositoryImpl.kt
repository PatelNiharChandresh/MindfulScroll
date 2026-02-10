package com.rudy.mindfulscroll.data.repository

import com.rudy.mindfulscroll.data.local.dao.UsageSessionDao
import com.rudy.mindfulscroll.data.local.entity.toDomain
import com.rudy.mindfulscroll.data.local.entity.toEntity
import com.rudy.mindfulscroll.domain.model.UsageSession
import com.rudy.mindfulscroll.domain.repository.UsageSessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UsageSessionRepositoryImpl @Inject constructor(
    private val dao: UsageSessionDao,
) : UsageSessionRepository {

    override suspend fun insert(session: UsageSession): Long =
        withContext(Dispatchers.IO) {
            dao.insert(session.toEntity())
        }

    override suspend fun getRecent(limit: Int): List<UsageSession> =
        withContext(Dispatchers.IO) {
            dao.getRecent(limit).map { it.toDomain() }
        }

    override suspend fun deleteOlderThan(cutoffTimestamp: Long) =
        withContext(Dispatchers.IO) {
            dao.deleteOlderThan(cutoffTimestamp)
        }
}
