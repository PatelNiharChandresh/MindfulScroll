package com.rudy.mindfulscroll.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ThresholdConfigTest {

    @Test
    fun `default threshold is Moderate 10-20-30`() {
        val config = ThresholdConfig.DEFAULT
        assertEquals("Moderate", config.presetName)
        assertEquals(10, config.threshold1Min)
        assertEquals(20, config.threshold2Min)
        assertEquals(30, config.threshold3Min)
    }

    @Test
    fun `light preset is 5-10-15`() {
        val config = ThresholdConfig.LIGHT
        assertEquals("Light", config.presetName)
        assertEquals(5, config.threshold1Min)
        assertEquals(10, config.threshold2Min)
        assertEquals(15, config.threshold3Min)
    }

    @Test
    fun `strict preset is 5-10-20`() {
        val config = ThresholdConfig.STRICT
        assertEquals("Strict", config.presetName)
        assertEquals(5, config.threshold1Min)
        assertEquals(10, config.threshold2Min)
        assertEquals(20, config.threshold3Min)
    }

    @Test
    fun `threshold1Seconds converts minutes to seconds`() {
        val config = ThresholdConfig.MODERATE
        assertEquals(600, config.threshold1Seconds)
    }

    @Test
    fun `threshold2Seconds converts minutes to seconds`() {
        val config = ThresholdConfig.MODERATE
        assertEquals(1200, config.threshold2Seconds)
    }

    @Test
    fun `threshold3Seconds converts minutes to seconds`() {
        val config = ThresholdConfig.MODERATE
        assertEquals(1800, config.threshold3Seconds)
    }

    @Test
    fun `throbTriggerSeconds is T3 plus 5 minutes`() {
        val config = ThresholdConfig.MODERATE
        // T3 = 30 min = 1800 sec, + 5 min = 300 sec = 2100 sec
        assertEquals(2100, config.throbTriggerSeconds)
    }

    @Test
    fun `throbTriggerSeconds with custom config`() {
        val config = ThresholdConfig(
            presetName = null,
            threshold1Min = 1,
            threshold2Min = 2,
            threshold3Min = 3,
        )
        // T3 = 3 min = 180 sec, + 5 min = 300 sec = 480 sec
        assertEquals(480, config.throbTriggerSeconds)
    }

    @Test
    fun `default config is global`() {
        val config = ThresholdConfig.DEFAULT
        assertEquals(true, config.isGlobal)
        assertNull(config.appPackageName)
    }

    @Test
    fun `copy preserves computed properties`() {
        val original = ThresholdConfig.MODERATE
        val modified = original.copy(threshold3Min = 60)
        assertEquals(3600, modified.threshold3Seconds)
        assertEquals(3900, modified.throbTriggerSeconds)
    }
}
