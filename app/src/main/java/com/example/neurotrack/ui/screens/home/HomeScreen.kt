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
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import com.example.neurotrack.data.preferences.UserPreferencesManager

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val viewModel = getViewModel<HomeViewModel>()
    val state by viewModel.state.collectAsState()
    val userPreferencesManager = koinInject<UserPreferencesManager>()
    val userName by userPreferencesManager.userName.collectAsState(initial = "")

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            TopBar(
                userName = userName,
                navController = navController
            )
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
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
                
                Text(
                    text = "Registros Recentes",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
                
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
                        onRecordClick = { recordId -> 
                            navController.navigate("record_details/$recordId") 
                        },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
} 