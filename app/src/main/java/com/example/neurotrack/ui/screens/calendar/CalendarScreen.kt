package com.example.neurotrack.ui.screens.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.neurotrack.ui.components.RecordsList
import com.example.neurotrack.ui.screens.calendar.components.CalendarContent
import com.example.neurotrack.ui.screens.calendar.components.CalendarHeader
import com.example.neurotrack.ui.screens.calendar.viewmodel.CalendarViewModel
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    onNavigateToAdd: (LocalDate, Boolean) -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    initialSelectedDate: LocalDate? = null,
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel = koinViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(initialSelectedDate) {
        initialSelectedDate?.let { date ->
            viewModel.selectDate(date)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            state.selectedDate?.let { selectedDate ->
                FloatingActionButton(
                    onClick = { 
                        onNavigateToAdd(selectedDate, true)
                    }
                ) {
                    Icon(Icons.Default.Add, "Adicionar registro")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            CalendarHeader(
                currentMonth = state.currentMonth,
                onPreviousMonth = { viewModel.updateMonth(state.currentMonth.minusMonths(1)) },
                onNextMonth = { viewModel.updateMonth(state.currentMonth.plusMonths(1)) },
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            CalendarContent(
                currentMonth = state.currentMonth,
                selectedDate = state.selectedDate,
                today = LocalDate.now(),
                markedDates = state.markedDates,
                onDayClick = { date -> viewModel.selectDate(date) },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )

            if (state.selectedDate != null) {
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                
                Text(
                    text = "Registros do dia",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )

                if (state.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (state.records.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Nenhum registro encontrado",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Adicione novos registros para visualizá-los aqui",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
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