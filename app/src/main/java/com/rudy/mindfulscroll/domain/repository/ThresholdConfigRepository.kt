package com.rudy.mindfulscroll.domain.repository

import com.rudy.mindfulscroll.domain.model.ThresholdConfig
import kotlinx.coroutines.flow.Flow

interface ThresholdConfigRepository {
    suspend fun getGlobal(): ThresholdConfig
    fun observeGlobal(): Flow<ThresholdConfig>
    suspend fun save(config: ThresholdConfig)
}
