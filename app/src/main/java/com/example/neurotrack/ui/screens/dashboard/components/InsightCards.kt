package com.example.neurotrack.ui.screens.dashboard.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InsightCards(
    mainInsight: String,
    patterns: List<String>,
    triggers: List<String>,
    recommendations: List<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        InsightCard(
            title = "Insight Principal",
            content = mainInsight
        )

        InsightCard(
            title = "Padrões Identificados",
            items = patterns
        )

        InsightCard(
            title = "Gatilhos Comuns",
            items = triggers
        )

        InsightCard(
            title = "Recomendações",
            items = recommendations
        )
    }
}

@Composable
private fun InsightCard(
    title: String,
    content: String? = null,
    items: List<String>? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (content != null) {
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (items != null) {
                Column {
                    items.forEach { item ->
                        Text(
                            text = "• $item",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
} 