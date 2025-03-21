package com.example.neurotrack.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavRoutes(val route: String, val icon: ImageVector, val contentDescription: String) {
    object Home : NavRoutes("home", Icons.Default.Home, "Início")
    object History : NavRoutes("history", Icons.Default.History, "Histórico")
    object Add : NavRoutes("add", Icons.Default.Add, "Adicionar")
    object Calendar : NavRoutes("calendar", Icons.Default.CalendarMonth, "Calendário")
    object Dashboard : NavRoutes("dashboard", Icons.Default.Dashboard, "Dashboard")

    companion object {
        fun bottomNavItems() = listOf(Home, History, Add, Calendar, Dashboard)
    }
} 