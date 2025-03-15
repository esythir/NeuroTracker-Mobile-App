package com.example.neurotrack.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

data class Record(
    val id: Long,
    val title: String,
    val description: String,
    val timestamp: LocalDateTime,
    val score: Int
)

@Composable
fun RecordsList(
    records: List<Record>,
    onRecordClick: (Record) -> Unit,
    onRefresh: () -> Unit,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = onRefresh,
    ) {
        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(records) { record ->
                RecordItem(
                    record = record,
                    onClick = { onRecordClick(record) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecordItem(
    record: Record,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = record.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = record.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = record.timestamp.format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                    ),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Badge(
                containerColor = when {
                    record.score >= 4 -> MaterialTheme.colorScheme.error
                    record.score >= 2 -> MaterialTheme.colorScheme.secondary
                    else -> MaterialTheme.colorScheme.primary
                }
            ) {
                Text(
                    text = "Intensidade: ${record.score}/5",
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
} 