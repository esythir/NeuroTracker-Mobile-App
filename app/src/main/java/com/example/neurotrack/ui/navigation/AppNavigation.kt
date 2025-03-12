package com.example.neurotrack.ui.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.neurotrack.navigation.NavRoutes
import com.example.neurotrack.ui.components.BottomBar
import com.example.neurotrack.ui.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavRoutes.Home.route
        ) {
            composable(NavRoutes.Home.route) {
                HomeScreen()
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