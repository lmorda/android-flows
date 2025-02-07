package com.lmorda.androidflows

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lmorda.androidflows.ui.AndroidFlowNavHost
import com.lmorda.androidflows.ui.SETTINGS_ROUTE
import com.lmorda.androidflows.ui.theme.AndroidFlowsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            AndroidFlowsTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                val appStateViewModel = hiltViewModel<AppStateViewModel>()

                LaunchedEffect(Unit) {
                    appStateViewModel.errorMessages.collect { message ->
                        if (message.isNotEmpty()) {
                            snackbarHostState.showSnackbar(message)
                        }
                    }
                }

                val navController = rememberNavController()
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStackEntry?.destination?.route

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = stringResource(R.string.health_monitor),
                                    style = MaterialTheme.typography.headlineLarge,
                                )
                            },
                            navigationIcon = {
                                when (currentRoute) {
                                    SETTINGS_ROUTE -> Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back",
                                        modifier = Modifier.clickable { navController.navigateUp() })

                                    else -> {}
                                }
                            }
                        )
                    },
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                ) { paddingValues ->
                    Surface(modifier = Modifier.padding(paddingValues)) {
                        AndroidFlowNavHost(navController = navController)
                    }
                }
            }
        }
    }
}