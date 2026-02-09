package com.rudy.mindfulscroll.util.constants

object AppConstants {
    // Polling
    const val POLL_INTERVAL_MS = 2000L
    const val QUERY_WINDOW_MS = 5000L
    const val FALLBACK_QUERY_WINDOW_MS = 10000L

    // Session
    const val COOLDOWN_DURATION_SECONDS = 45
    const val OVERLAY_APPEAR_THRESHOLD_SECONDS = 60
    const val THROB_DELAY_AFTER_T3_SECONDS = 300 // 5 minutes
    const val SESSION_RETENTION_DAYS = 90L

    // Overlay Dimensions
    const val OVERLAY_SIZE_DP = 52
    const val OVERLAY_TEXT_SIZE_SP = 12f

    // Overlay Animations
    const val FADE_IN_DURATION_MS = 500L
    const val FADE_OUT_DURATION_MS = 400L
    const val COLOR_TRANSITION_DURATION_MS = 2000L
    const val SNAP_ANIMATION_DURATION_MS = 250L
    const val THROB_CYCLE_DURATION_MS = 1500L
    const val THROB_SCALE_MAX = 1.15f
    const val COOLDOWN_DIM_ALPHA_REDUCTION = 0.30f
    const val DIM_UNDIM_DURATION_MS = 300L

    // Drag Detection
    const val TAP_TIMEOUT_MS = 150L
    const val TAP_SLOP_DP = 10

    // Defaults
    const val DEFAULT_OVERLAY_EDGE = "right"
    const val DEFAULT_OVERLAY_Y_PERCENT = 0.5f
    const val DEFAULT_THRESHOLD_PRESET = "Moderate"
    const val DEFAULT_T1 = 10
    const val DEFAULT_T2 = 20
    const val DEFAULT_T3 = 30

    // Notification
    const val NOTIFICATION_CHANNEL_ID = "mindfulscroll_monitoring"
    const val NOTIFICATION_CHANNEL_NAME = "Monitoring Service"
    const val NOTIFICATION_ID = 1001

    // Preference Keys
    const val PREF_MONITORING_ACTIVE = "monitoring_active"
    const val PREF_ONBOARDING_COMPLETED = "onboarding_completed"
    const val PREF_OVERLAY_POSITION_EDGE = "overlay_position_edge"
    const val PREF_OVERLAY_POSITION_Y_PERCENT = "overlay_position_y_percent"
    const val PREF_COOLDOWN_DURATION_SECONDS = "cooldown_duration_seconds"
}
