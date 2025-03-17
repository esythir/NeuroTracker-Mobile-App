package com.example.neurotrack.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.neurotrack.navigation.NavRoutes
import com.example.neurotrack.ui.components.BottomBar
import com.example.neurotrack.ui.screens.addscreen.AddScreen
import com.example.neurotrack.ui.screens.dashboard.DashboardScreen
import com.example.neurotrack.ui.screens.history.HistoryScreen
import com.example.neurotrack.ui.screens.home.HomeScreen
import com.example.neurotrack.ui.screens.calendar.CalendarScreen
import androidx.navigation.NavHostController
import java.time.LocalDate
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.neurotrack.ui.screens.dashboard.DashboardViewModel
import com.example.neurotrack.ui.screens.settings.SettingsScreen
import org.koin.androidx.compose.getViewModel

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(Screen.Calendar.route) {
            val returnDate = navController.currentBackStackEntry
                ?.savedStateHandle
                ?.get<String>("returnDate")
                ?.let { LocalDate.parse(it) }

            CalendarScreen(
                onNavigateToAdd = { date, isFromCalendar ->
                    navController.navigate("${Screen.Add.route}?date=${date}&fromCalendar=${isFromCalendar}")
                },
                onNavigateToDetail = { recordId ->
                    navController.navigate("${Screen.Detail.route}/$recordId")
                },
                initialSelectedDate = returnDate
            )
        }

        composable(
            route = "${Screen.Add.route}?date={date}&fromCalendar={fromCalendar}",
            arguments = listOf(
                navArgument("date") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("fromCalendar") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { backStackEntry ->
            val date = backStackEntry.arguments?.getString("date")?.let {
                LocalDate.parse(it)
            }
            val isFromCalendar = backStackEntry.arguments?.getBoolean("fromCalendar") ?: false

            AddScreen(
                initialDate = date,
                isFromCalendar = isFromCalendar,
                onNavigateBack = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("returnDate", date.toString())
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateToCalendar = {
                    navController.navigate(Screen.Calendar.route) {
                        popUpTo(Screen.Calendar.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.History.route) {
            HistoryScreen()
        }

        composable(
            route = Screen.Dashboard.route
        ) {
            val viewModel = getViewModel<DashboardViewModel>()
            DashboardScreen(viewModel = viewModel)
        }

        composable(
            route = Screen.Settings.route
        ) { backStackEntry ->
            SettingsScreen(
                navController = navController
            )
        }
    }
}

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object History : Screen("history")
    object Add : Screen("add")
    object Calendar : Screen("calendar")
    object Dashboard : Screen("dashboard")
    object Detail : Screen("detail")
    object Settings : Screen("settings")
}