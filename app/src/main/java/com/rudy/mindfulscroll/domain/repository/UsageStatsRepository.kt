package com.rudy.mindfulscroll.domain.repository

interface UsageStatsRepository {
    suspend fun getCurrentForegroundPackage(): String?
}
