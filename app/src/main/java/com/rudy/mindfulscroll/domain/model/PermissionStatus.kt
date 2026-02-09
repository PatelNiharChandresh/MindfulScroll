package com.rudy.mindfulscroll.domain.model

data class PermissionStatus(
    val usageAccessGranted: Boolean = false,
    val overlayGranted: Boolean = false,
    val batteryOptimizationExempted: Boolean = false,
    val notificationGranted: Boolean = false,
) {
    val allCriticalGranted: Boolean
        get() = usageAccessGranted && overlayGranted

    val allGranted: Boolean
        get() = usageAccessGranted && overlayGranted &&
            batteryOptimizationExempted && notificationGranted
}
