package com.example.neurotrack.ui.screens.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CalendarContent(
    currentMonth: YearMonth,
    selectedDate: LocalDate?,
    today: LocalDate,
    markedDates: Set<LocalDate>,
    onDayClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val daysInWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    
    Column(modifier = modifier) {
        // Dias da semana
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            daysInWeek.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Dias do mês
        val firstDayOfMonth = currentMonth.atDay(1)
        val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
        val totalDays = currentMonth.lengthOfMonth()
        val totalWeeks = (totalDays + firstDayOfWeek + 6) / 7

        repeat(totalWeeks) { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(7) { dayOfWeek ->
                    val day = week * 7 + dayOfWeek - firstDayOfWeek + 1
                    val date = if (day in 1..totalDays) {
                        currentMonth.atDay(day)
                    } else null

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(2.dp)
                            .then(
                                if (date != null) {
                                    Modifier.clickable { onDayClick(date) }
                                } else Modifier
                            )
                            .background(
                                when {
                                    date == selectedDate -> MaterialTheme.colorScheme.primary
                                    date in markedDates -> MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                                    date == today -> MaterialTheme.colorScheme.primaryContainer
                                    else -> Color.Transparent
                                },
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (date != null) {
                            Text(
                                text = date.dayOfMonth.toString(),
                                color = when {
                                    date == selectedDate -> MaterialTheme.colorScheme.onPrimary
                                    date in markedDates -> MaterialTheme.colorScheme.onPrimary
                                    date == today -> MaterialTheme.colorScheme.onPrimaryContainer
                                    else -> LocalContentColor.current
                                },
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
} 