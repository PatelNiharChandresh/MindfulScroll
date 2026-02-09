package com.rudy.mindfulscroll.util.extensions

import org.junit.Assert.assertEquals
import org.junit.Test

class TimeFormatExtensionsTest {

    @Test
    fun `zero seconds formats as 00 colon 00`() {
        assertEquals("00:00", 0.toFormattedTime())
    }

    @Test
    fun `59 seconds formats as 00 colon 59`() {
        assertEquals("00:59", 59.toFormattedTime())
    }

    @Test
    fun `60 seconds formats as 01 colon 00`() {
        assertEquals("01:00", 60.toFormattedTime())
    }

    @Test
    fun `90 seconds formats as 01 colon 30`() {
        assertEquals("01:30", 90.toFormattedTime())
    }

    @Test
    fun `3600 seconds formats as 60 colon 00`() {
        assertEquals("60:00", 3600.toFormattedTime())
    }

    @Test
    fun `7261 seconds formats as 121 colon 01`() {
        assertEquals("121:01", 7261.toFormattedTime())
    }
}
