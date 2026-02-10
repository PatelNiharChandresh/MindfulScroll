package com.rudy.mindfulscroll.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rudy.mindfulscroll.data.local.entity.ThresholdConfigEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ThresholdConfigDao {

    @Query("SELECT * FROM threshold_configs WHERE isGlobal = 1 LIMIT 1")
    suspend fun getGlobal(): ThresholdConfigEntity?

    @Query("SELECT * FROM threshold_configs WHERE isGlobal = 1 LIMIT 1")
    fun observeGlobal(): Flow<ThresholdConfigEntity?>

    @Upsert
    suspend fun upsert(config: ThresholdConfigEntity)
}
