package com.lmorda.androidflows.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lmorda.androidflows.ui.settings.SettingsScreen
import com.lmorda.androidflows.ui.vitals.VitalSignsScreen

const val VITAL_SIGNS_ROUTE = "vitalSigns"
const val SETTINGS_ROUTE = "settings"

@Composable
internal fun AndroidFlowNavHost(
    navController: NavHostController,
) {
    NavHost(navController = navController, startDestination = VITAL_SIGNS_ROUTE) {
        composable(VITAL_SIGNS_ROUTE) {
            VitalSignsScreen(
                viewModel = hiltViewModel(),
                navigateToSettings = { navController.navigate(SETTINGS_ROUTE) }
            )
        }
        composable(SETTINGS_ROUTE) {
            SettingsScreen(
                viewModel = hiltViewModel(),
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}