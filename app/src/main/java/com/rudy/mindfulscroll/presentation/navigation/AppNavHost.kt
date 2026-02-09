package com.rudy.mindfulscroll.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        // Onboarding flow
        composable(Route.Welcome.route) {
            PlaceholderScreen("Welcome")
        }
        composable(Route.PermUsageAccess.route) {
            PlaceholderScreen("Usage Access Permission")
        }
        composable(Route.PermOverlay.route) {
            PlaceholderScreen("Overlay Permission")
        }
        composable(Route.PermBattery.route) {
            PlaceholderScreen("Battery Optimization")
        }
        composable(Route.OnboardComplete.route) {
            PlaceholderScreen("Onboarding Complete")
        }

        // Main flow
        composable(Route.Home.route) {
            PlaceholderScreen("Home")
        }
        composable(Route.AppSelection.route) {
            PlaceholderScreen("App Selection")
        }
        composable(Route.ThresholdConfig.route) {
            PlaceholderScreen("Threshold Configuration")
        }
        composable(Route.About.route) {
            PlaceholderScreen("About")
        }
    }
}

@Composable
private fun PlaceholderScreen(name: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = name)
    }
}
