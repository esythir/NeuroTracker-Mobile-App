package com.example.neurotrack.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.neurotrack.ui.components.HomeTopBar
import com.example.neurotrack.ui.components.HomeStatsGrid

@Composable
fun HomeScreen(
    onSettingsClick: () -> Unit = {},
    onDashboardClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp) // Espaço para a BottomBar
        ) {
            HomeTopBar(
                patientName = "João Silva",
                onSettingsClick = onSettingsClick
            )
            
            HomeStatsGrid(
                recordCount = 42,
                onDashboardClick = onDashboardClick
            )
            
            // Espaço para debug
            Text(
                text = "Home Screen",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
} 