package com.rudy.mindfulscroll.domain.model

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PermissionStatusTest {

    @Test
    fun `allCriticalGranted requires usage access and overlay`() {
        val status = PermissionStatus(
            usageAccessGranted = true,
            overlayGranted = true,
            batteryOptimizationExempted = false,
            notificationGranted = false,
        )
        assertTrue(status.allCriticalGranted)
    }

    @Test
    fun `allCriticalGranted is false when usage access missing`() {
        val status = PermissionStatus(
            usageAccessGranted = false,
            overlayGranted = true,
        )
        assertFalse(status.allCriticalGranted)
    }

    @Test
    fun `allCriticalGranted is false when overlay missing`() {
        val status = PermissionStatus(
            usageAccessGranted = true,
            overlayGranted = false,
        )
        assertFalse(status.allCriticalGranted)
    }

    @Test
    fun `allGranted requires all four permissions`() {
        val status = PermissionStatus(
            usageAccessGranted = true,
            overlayGranted = true,
            batteryOptimizationExempted = true,
            notificationGranted = true,
        )
        assertTrue(status.allGranted)
    }

    @Test
    fun `allGranted is false when any permission missing`() {
        val status = PermissionStatus(
            usageAccessGranted = true,
            overlayGranted = true,
            batteryOptimizationExempted = true,
            notificationGranted = false,
        )
        assertFalse(status.allGranted)
    }

    @Test
    fun `default status has all permissions false`() {
        val status = PermissionStatus()
        assertFalse(status.usageAccessGranted)
        assertFalse(status.overlayGranted)
        assertFalse(status.batteryOptimizationExempted)
        assertFalse(status.notificationGranted)
        assertFalse(status.allCriticalGranted)
        assertFalse(status.allGranted)
    }
}
