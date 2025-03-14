package com.example.neurotrack.ui.screens.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.neurotrack.ui.screens.calendar.components.CalendarContent
import com.example.neurotrack.ui.screens.calendar.components.CalendarHeader
import com.example.neurotrack.ui.screens.calendar.viewmodel.CalendarViewModel
import java.time.YearMonth
import java.time.LocalDate
import org.koin.androidx.compose.koinViewModel

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Current Month: ${state.currentMonth}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )

        CalendarHeader(
            currentMonth = state.currentMonth,
            onPreviousMonth = { /* TODO: viewModel.updateMonth */ },
            onNextMonth = { /* TODO: viewModel.updateMonth */ }
        )
        
        CalendarContent(
            currentMonth = state.currentMonth,
            onDayClick = { /* TODO: viewModel.selectDate */ }
        )

        state.selectedDate?.let { date ->
            Text(
                text = "Selected Date: $date",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
} 