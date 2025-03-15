package com.example.neurotrack.ui.screens.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.neurotrack.ui.components.RecordsList
import com.example.neurotrack.ui.screens.calendar.components.CalendarContent
import com.example.neurotrack.ui.screens.calendar.components.CalendarHeader
import com.example.neurotrack.ui.screens.calendar.viewmodel.CalendarViewModel
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    onNavigateToAdd: (LocalDate, Boolean) -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    initialSelectedDate: LocalDate? = null,
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    // Efeito para selecionar a data inicial
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
            // Cabeçalho do Calendário
            CalendarHeader(
                currentMonth = state.currentMonth,
                onPreviousMonth = { viewModel.updateMonth(state.currentMonth.minusMonths(1)) },
                onNextMonth = { viewModel.updateMonth(state.currentMonth.plusMonths(1)) },
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // Calendário
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

            // Lista de Registros
            if (state.selectedDate != null) {
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                
                Text(
                    text = "Registros do dia",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )

                RecordsList(
                    records = state.records,
                    onRecordClick = { record -> onNavigateToDetail(record.id) },
                    onRefresh = { 
                        state.selectedDate?.let { viewModel.selectDate(it) }
                    },
                    isRefreshing = state.isLoading,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }
} 