package com.rudy.mindfulscroll.data.local.entity

import com.rudy.mindfulscroll.domain.model.MonitoredApp
import com.rudy.mindfulscroll.domain.model.ThresholdConfig
import com.rudy.mindfulscroll.domain.model.UsageSession
import org.junit.Assert.assertEquals
import org.junit.Test

class MappersTest {

    // --- MonitoredApp ---

    @Test
    fun `MonitoredAppEntity toDomain maps all fields`() {
        val entity = MonitoredAppEntity(
            packageName = "com.example.app",
            displayName = "Example App",
            isActive = true,
            addedAt = 1000L,
        )
        val domain = entity.toDomain()
        assertEquals("com.example.app", domain.packageName)
        assertEquals("Example App", domain.displayName)
        assertEquals(true, domain.isActive)
        assertEquals(1000L, domain.addedAt)
    }

    @Test
    fun `MonitoredApp toEntity maps all fields`() {
        val domain = MonitoredApp(
            packageName = "com.example.app",
            displayName = "Example App",
            isActive = false,
            addedAt = 2000L,
        )
        val entity = domain.toEntity()
        assertEquals("com.example.app", entity.packageName)
        assertEquals("Example App", entity.displayName)
        assertEquals(false, entity.isActive)
        assertEquals(2000L, entity.addedAt)
    }

    @Test
    fun `MonitoredApp round-trip is lossless`() {
        val original = MonitoredApp(
            packageName = "com.test",
            displayName = "Test",
            isActive = true,
            addedAt = 12345L,
        )
        val roundTripped = original.toEntity().toDomain()
        assertEquals(original, roundTripped)
    }

    // --- ThresholdConfig ---

    @Test
    fun `ThresholdConfigEntity toDomain maps all fields`() {
        val entity = ThresholdConfigEntity(
            id = 5,
            presetName = "Moderate",
            threshold1Min = 10,
            threshold2Min = 20,
            threshold3Min = 30,
            isGlobal = true,
            appPackageName = null,
        )
        val domain = entity.toDomain()
        assertEquals(5, domain.id)
        assertEquals("Moderate", domain.presetName)
        assertEquals(10, domain.threshold1Min)
        assertEquals(20, domain.threshold2Min)
        assertEquals(30, domain.threshold3Min)
        assertEquals(true, domain.isGlobal)
        assertEquals(null, domain.appPackageName)
    }

    @Test
    fun `ThresholdConfig toEntity maps all fields including nullable`() {
        val domain = ThresholdConfig(
            id = 3,
            presetName = null,
            threshold1Min = 5,
            threshold2Min = 15,
            threshold3Min = 25,
            isGlobal = false,
            appPackageName = "com.example.app",
        )
        val entity = domain.toEntity()
        assertEquals(3, entity.id)
        assertEquals(null, entity.presetName)
        assertEquals(5, entity.threshold1Min)
        assertEquals(15, entity.threshold2Min)
        assertEquals(25, entity.threshold3Min)
        assertEquals(false, entity.isGlobal)
        assertEquals("com.example.app", entity.appPackageName)
    }

    @Test
    fun `ThresholdConfig round-trip is lossless`() {
        val original = ThresholdConfig(
            id = 1,
            presetName = "Light",
            threshold1Min = 5,
            threshold2Min = 10,
            threshold3Min = 15,
            isGlobal = true,
            appPackageName = null,
        )
        val roundTripped = original.toEntity().toDomain()
        assertEquals(original, roundTripped)
    }

    // --- UsageSession ---

    @Test
    fun `UsageSessionEntity toDomain maps all fields`() {
        val entity = UsageSessionEntity(
            id = 42,
            appPackageName = "com.example.app",
            startTimestamp = 1000L,
            endTimestamp = 2000L,
            durationSeconds = 1000,
            maxThresholdReached = 3,
            throbActivated = true,
        )
        val domain = entity.toDomain()
        assertEquals(42L, domain.id)
        assertEquals("com.example.app", domain.appPackageName)
        assertEquals(1000L, domain.startTimestamp)
        assertEquals(2000L, domain.endTimestamp)
        assertEquals(1000, domain.durationSeconds)
        assertEquals(3, domain.maxThresholdReached)
        assertEquals(true, domain.throbActivated)
    }

    @Test
    fun `UsageSession toEntity maps all fields`() {
        val domain = UsageSession(
            id = 10,
            appPackageName = "com.test",
            startTimestamp = 500L,
            endTimestamp = 1500L,
            durationSeconds = 900,
            maxThresholdReached = 2,
            throbActivated = false,
        )
        val entity = domain.toEntity()
        assertEquals(10L, entity.id)
        assertEquals("com.test", entity.appPackageName)
        assertEquals(500L, entity.startTimestamp)
        assertEquals(1500L, entity.endTimestamp)
        assertEquals(900, entity.durationSeconds)
        assertEquals(2, entity.maxThresholdReached)
        assertEquals(false, entity.throbActivated)
    }

    @Test
    fun `UsageSession round-trip is lossless`() {
        val original = UsageSession(
            id = 7,
            appPackageName = "com.round.trip",
            startTimestamp = 100L,
            endTimestamp = 200L,
            durationSeconds = 100,
            maxThresholdReached = 4,
            throbActivated = true,
        )
        val roundTripped = original.toEntity().toDomain()
        assertEquals(original, roundTripped)
    }
}
