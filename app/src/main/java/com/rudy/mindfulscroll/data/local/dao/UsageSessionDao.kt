package com.rudy.mindfulscroll.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rudy.mindfulscroll.data.local.entity.UsageSessionEntity

@Dao
interface UsageSessionDao {

    @Insert
    suspend fun insert(session: UsageSessionEntity): Long

    @Query("SELECT * FROM usage_sessions ORDER BY startTimestamp DESC LIMIT :limit")
    suspend fun getRecent(limit: Int = 100): List<UsageSessionEntity>

    @Query("DELETE FROM usage_sessions WHERE endTimestamp < :cutoffTimestamp")
    suspend fun deleteOlderThan(cutoffTimestamp: Long)
}
