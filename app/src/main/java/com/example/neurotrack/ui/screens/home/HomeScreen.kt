package com.example.neurotrack.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.neurotrack.ui.screens.home.components.HomeTopBar
import com.example.neurotrack.ui.screens.home.components.HomeStatsGrid
import com.example.neurotrack.ui.components.RecordsList
import com.example.neurotrack.ui.components.TopBar
import org.koin.androidx.compose.getViewModel
import androidx.navigation.NavController

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val viewModel = getViewModel<HomeViewModel>()
    val state by viewModel.state.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Adicionar a TopBar com o botão de configurações
            TopBar(
                userName = "João Silva", // Você pode obter isso do ViewModel
                navController = navController
            )
            
            // Conteúdo principal com padding
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                // Cards de estatísticas com sombra e cantos arredondados
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    HomeStatsGrid(
                        recordCount = state.stats.totalRecords,
                        averageScore = state.stats.averageScore,
                        predominantMood = state.stats.predominantMood,
                        moodCount = state.stats.moodCount,
                        onDashboardClick = { /* TODO: Implementar navegação para dashboard */ },
                        modifier = Modifier.padding(16.dp)
                    )
                }
                
                // Título da seção de registros recentes
                Text(
                    text = "Registros Recentes",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
                
                // Lista de registros com design melhorado
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    RecordsList(
                        records = state.records,
                        onRecordClick = { record -> viewModel.onRecordClick(record) },
                        onRefresh = { viewModel.onRefresh() },
                        isRefreshing = state.isRefreshing,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
} 