package com.example.neurotrack.ui.screens.history

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.neurotrack.ui.components.RecordsList
import org.koin.androidx.compose.koinViewModel

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "History",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        RecordsList(
            records = state.records,
            onRecordClick = { record -> viewModel.onRecordClick(record) },
            onRefresh = { viewModel.onRefresh() },
            isRefreshing = state.isRefreshing
        )
    }
} 