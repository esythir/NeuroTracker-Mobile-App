package com.example.neurotrack.ui.screens.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.neurotrack.ui.screens.calendar.components.CalendarContent
import com.example.neurotrack.ui.screens.calendar.components.CalendarHeader
import com.example.neurotrack.ui.screens.calendar.viewmodel.CalendarViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    calendarViewModel: CalendarViewModel = koinViewModel()
) {
    val state by calendarViewModel.state.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CalendarHeader(
            currentMonth = state.currentMonth,
            onPreviousMonth = { calendarViewModel.updateMonth(state.currentMonth.minusMonths(1)) },
            onNextMonth = { calendarViewModel.updateMonth(state.currentMonth.plusMonths(1)) }
        )

        CalendarContent(
            currentMonth = state.currentMonth,
            selectedDate = state.selectedDate,
            today = state.today,
            markedDates = state.markedDates,
            onDayClick = { calendarViewModel.selectDate(it) }
        )

        state.selectedDate?.let { date ->
            Text(
                text = "Selected Date: ${date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // HeatMap Calendar
        HeatMapCalendar(
            startDate = LocalDate.now().minusMonths(6),
            endDate = LocalDate.now(),
            values = state.markedDates
        )
    }
}

@Composable
fun HeatMapCalendar(
    startDate: LocalDate,
    endDate: LocalDate,
    values: Set<LocalDate>,
    modifier: Modifier = Modifier
) {
    val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", 
                       "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    
    val dateList = remember {
        val list = mutableListOf<LocalDate>()
        var currentDate = startDate
        while (!currentDate.isAfter(endDate)) {
            list.add(currentDate)
            currentDate = currentDate.plusDays(1)
        }
        list
    }

    Column(modifier = modifier.fillMaxWidth()) {
        // Meses
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            months.forEach { month ->
                Text(
                    text = month,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }

        // Grid de dias
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Dias da semana
            Column {
                daysOfWeek.forEach { day ->
                    Text(
                        text = day,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }

            // Grid de contribuições
            LazyRow(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                items(dateList) { date ->
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .padding(1.dp)
                            .background(
                                color = when {
                                    date in values -> MaterialTheme.colorScheme.primary
                                    else -> MaterialTheme.colorScheme.surfaceVariant
                                },
                                shape = RoundedCornerShape(2.dp)
                            )
                    )
                }
            }
        }
    }
} 