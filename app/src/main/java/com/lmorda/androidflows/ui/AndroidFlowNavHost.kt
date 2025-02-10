package com.lmorda.androidflows.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lmorda.androidflows.ui.settings.SettingsScreen
import com.lmorda.androidflows.ui.settings.SettingsViewModel
import com.lmorda.androidflows.ui.vitals.VitalSignsScreen
import com.lmorda.androidflows.ui.vitals.VitalSignsViewModel

const val VITAL_SIGNS_ROUTE = "vitalSigns"
const val SETTINGS_ROUTE = "settings"

@Composable
internal fun AndroidFlowNavHost(
    navController: NavHostController,
) {
    NavHost(navController = navController, startDestination = VITAL_SIGNS_ROUTE) {
        composable(VITAL_SIGNS_ROUTE) {
            val vitalSignsViewModel: VitalSignsViewModel = hiltViewModel()
            val state = vitalSignsViewModel.state.collectAsStateWithLifecycle().value
            VitalSignsScreen(
                state = state,
                navigateToSettings = { navController.navigate(SETTINGS_ROUTE) }
            )
        }
        composable(SETTINGS_ROUTE) {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val state = settingsViewModel.state.collectAsStateWithLifecycle().value
            SettingsScreen(
                state = state,
                onNavigateBack = { navController.popBackStack() },
                onUpdateUser = { settingsViewModel.updateUser(it.firstName, it.lastName) },
            )
        }
    }
}