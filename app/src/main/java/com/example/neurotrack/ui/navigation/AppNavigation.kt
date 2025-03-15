package com.example.neurotrack.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.neurotrack.navigation.NavRoutes
import com.example.neurotrack.ui.components.BottomBar
import com.example.neurotrack.ui.screens.AddScreen
import com.example.neurotrack.ui.screens.DashboardScreen
import com.example.neurotrack.ui.screens.history.HistoryScreen
import com.example.neurotrack.ui.screens.home.HomeScreen
import com.example.neurotrack.ui.screens.calendar.CalendarScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavRoutes.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(NavRoutes.Home.route) {
                HomeScreen(
                    onSettingsClick = { /* TODO */ },
                    onDashboardClick = { navController.navigate(NavRoutes.Dashboard.route) }
                )
            }
            composable(NavRoutes.History.route) {
                HistoryScreen()
            }
            composable(NavRoutes.Add.route) {
                AddScreen()
            }
            composable(NavRoutes.Calendar.route) {
                CalendarScreen()
            }
            composable(NavRoutes.Dashboard.route) {
                DashboardScreen()
            }
        }
    }
}