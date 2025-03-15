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
import com.example.neurotrack.ui.screens.addscreen.AddScreen
import com.example.neurotrack.ui.screens.DashboardScreen
import com.example.neurotrack.ui.screens.history.HistoryScreen
import com.example.neurotrack.ui.screens.home.HomeScreen
import com.example.neurotrack.ui.screens.calendar.CalendarScreen
import androidx.navigation.NavHostController

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String = NavRoutes.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
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

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object History : Screen("history")
    object Add : Screen("add")
    object Calendar : Screen("calendar")
    object Dashboard : Screen("dashboard")
}