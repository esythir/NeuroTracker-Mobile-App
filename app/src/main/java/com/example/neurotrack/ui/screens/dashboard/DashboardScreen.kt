package com.example.neurotrack.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import com.example.neurotrack.ui.screens.dashboard.components.InsightCards

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Análise de Comportamento",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else if (state.error != null) {
            ErrorMessage(
                message = state.error,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            DashboardContent(state = state)
        }
    }
}

@Composable
private fun DashboardContent(
    state: DashboardState,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        item {
            SummaryCard(
                totalRecords = state.totalRecords,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Por enquanto, vamos mostrar uma versão offline dos insights
        item {
            InsightCards(
                mainInsight = state.mainInsight.ifEmpty { "Colete mais registros para obter insights personalizados" },
                patterns = if (state.patterns.isEmpty()) {
                    listOf("Ainda não há padrões suficientes para análise")
                } else state.patterns,
                triggers = if (state.triggers.isEmpty()) {
                    listOf("Continue registrando para identificar gatilhos comuns")
                } else state.triggers,
                recommendations = if (state.recommendations.isEmpty()) {
                    listOf(
                        "Mantenha registros regulares",
                        "Observe padrões de comportamento",
                        "Identifique possíveis gatilhos"
                    )
                } else state.recommendations,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun SummaryCard(
    totalRecords: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Resumo dos últimos 30 dias",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "$totalRecords registros realizados",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun ErrorMessage(
    message: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Ops! Algo deu errado",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = message ?: "Erro desconhecido",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
} 