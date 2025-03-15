package com.example.neurotrack.ui.screens.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeStatsGrid(
    recordCount: Int,
    averageScore: Float,
    predominantMood: String,
    moodCount: Int,
    onDashboardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Total de Registros
            StatsCard(
                icon = Icons.Default.Assignment,
                label = "Total Registros",
                value = recordCount.toString(),
                onClick = onDashboardClick,
                modifier = Modifier.weight(1f)
            )
            
            // Média de Pontuação
            StatsCard(
                icon = Icons.Default.Stars,
                label = "Média",
                value = String.format("%.1f", averageScore),
                onClick = { /* TODO */ },
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Card largo do Humor Predominante
        Card(
            onClick = { /* TODO */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Mood,
                    contentDescription = "Humor Predominante",
                    modifier = Modifier.size(24.dp)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = predominantMood,
                    style = MaterialTheme.typography.titleLarge
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "Humor Predominante",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "Registrado $moodCount vezes",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatsCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            if (value != null) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
            
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
} 