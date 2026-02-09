package com.rudy.mindfulscroll.domain.repository

import com.rudy.mindfulscroll.domain.model.UsageSession

interface UsageSessionRepository {
    suspend fun insert(session: UsageSession): Long
    suspend fun getRecent(limit: Int = 100): List<UsageSession>
    suspend fun deleteOlderThan(cutoffTimestamp: Long)
}
