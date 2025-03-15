package com.example.neurotrack.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.neurotrack.ui.screens.home.components.HomeTopBar
import com.example.neurotrack.ui.screens.home.components.HomeStatsGrid
import com.example.neurotrack.ui.components.RecordsList
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    println("DEBUG UI: Total Records = ${state.stats.totalRecords}")
    println("DEBUG UI: Average = ${state.stats.averageScore}")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        HomeTopBar(
            patientName = "João Silva",
            onSettingsClick = {}
        )
        
        HomeStatsGrid(
            recordCount = state.stats.totalRecords,
            averageScore = state.stats.averageScore,
            predominantMood = state.stats.predominantMood,
            moodCount = state.stats.moodCount,
            onDashboardClick = { /* TODO: Implementar navegação para dashboard */ },
            modifier = Modifier
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Recent Records",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        
        RecordsList(
            records = state.records,
            onRecordClick = { record -> viewModel.onRecordClick(record) },
            onRefresh = { viewModel.onRefresh() },
            isRefreshing = state.isRefreshing,
            modifier = Modifier.weight(1f)
        )
    }
} 