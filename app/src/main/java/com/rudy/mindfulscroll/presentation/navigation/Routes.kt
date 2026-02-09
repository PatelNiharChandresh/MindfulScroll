package com.rudy.mindfulscroll.presentation.navigation

sealed class Route(val route: String) {
    // Onboarding
    data object Welcome : Route("onboarding/welcome")
    data object PermUsageAccess : Route("onboarding/perm_usage_access")
    data object PermOverlay : Route("onboarding/perm_overlay")
    data object PermBattery : Route("onboarding/perm_battery")
    data object OnboardComplete : Route("onboarding/complete")

    // Main
    data object Home : Route("main/home")
    data object AppSelection : Route("main/app_selection")
    data object ThresholdConfig : Route("main/threshold_config")
    data object About : Route("main/about")
}
