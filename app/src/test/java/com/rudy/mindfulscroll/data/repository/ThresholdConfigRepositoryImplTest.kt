package com.rudy.mindfulscroll.data.repository

import com.rudy.mindfulscroll.data.local.dao.ThresholdConfigDao
import com.rudy.mindfulscroll.data.local.entity.ThresholdConfigEntity
import com.rudy.mindfulscroll.domain.model.ThresholdConfig
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ThresholdConfigRepositoryImplTest {

    private lateinit var dao: ThresholdConfigDao
    private lateinit var repository: ThresholdConfigRepositoryImpl

    @Before
    fun setup() {
        dao = mockk(relaxed = true)
        repository = ThresholdConfigRepositoryImpl(dao)
    }

    @Test
    fun `getGlobal returns domain model when entity exists`() = runTest {
        val entity = ThresholdConfigEntity(
            id = 1,
            presetName = "Moderate",
            threshold1Min = 10,
            threshold2Min = 20,
            threshold3Min = 30,
            isGlobal = true,
        )
        coEvery { dao.getGlobal() } returns entity

        val result = repository.getGlobal()

        assertEquals(1, result.id)
        assertEquals("Moderate", result.presetName)
        assertEquals(10, result.threshold1Min)
        assertEquals(20, result.threshold2Min)
        assertEquals(30, result.threshold3Min)
    }

    @Test
    fun `getGlobal returns DEFAULT when no entity exists`() = runTest {
        coEvery { dao.getGlobal() } returns null

        val result = repository.getGlobal()

        assertEquals(ThresholdConfig.DEFAULT, result)
    }

    @Test
    fun `observeGlobal maps entity to domain model`() = runTest {
        val entity = ThresholdConfigEntity(
            id = 2,
            presetName = "Light",
            threshold1Min = 5,
            threshold2Min = 10,
            threshold3Min = 15,
            isGlobal = true,
        )
        coEvery { dao.observeGlobal() } returns flowOf(entity)

        val result = repository.observeGlobal().first()

        assertEquals("Light", result.presetName)
        assertEquals(5, result.threshold1Min)
    }

    @Test
    fun `observeGlobal returns DEFAULT when entity is null`() = runTest {
        coEvery { dao.observeGlobal() } returns flowOf(null)

        val result = repository.observeGlobal().first()

        assertEquals(ThresholdConfig.DEFAULT, result)
    }

    @Test
    fun `save converts domain to entity and delegates to dao`() = runTest {
        val config = ThresholdConfig(
            id = 1,
            presetName = "Strict",
            threshold1Min = 5,
            threshold2Min = 10,
            threshold3Min = 20,
        )

        repository.save(config)

        coVerify {
            dao.upsert(
                ThresholdConfigEntity(
                    id = 1,
                    presetName = "Strict",
                    threshold1Min = 5,
                    threshold2Min = 10,
                    threshold3Min = 20,
                    isGlobal = true,
                    appPackageName = null,
                )
            )
        }
    }
}
