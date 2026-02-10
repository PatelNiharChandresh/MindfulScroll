package com.rudy.mindfulscroll.data.repository

import com.rudy.mindfulscroll.data.local.dao.ThresholdConfigDao
import com.rudy.mindfulscroll.data.local.entity.toDomain
import com.rudy.mindfulscroll.data.local.entity.toEntity
import com.rudy.mindfulscroll.domain.model.ThresholdConfig
import com.rudy.mindfulscroll.domain.repository.ThresholdConfigRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ThresholdConfigRepositoryImpl @Inject constructor(
    private val dao: ThresholdConfigDao,
) : ThresholdConfigRepository {

    override suspend fun getGlobal(): ThresholdConfig =
        withContext(Dispatchers.IO) {
            dao.getGlobal()?.toDomain() ?: ThresholdConfig.DEFAULT
        }

    override fun observeGlobal(): Flow<ThresholdConfig> =
        dao.observeGlobal().map { entity ->
            entity?.toDomain() ?: ThresholdConfig.DEFAULT
        }

    override suspend fun save(config: ThresholdConfig) =
        withContext(Dispatchers.IO) {
            dao.upsert(config.toEntity())
        }
}
