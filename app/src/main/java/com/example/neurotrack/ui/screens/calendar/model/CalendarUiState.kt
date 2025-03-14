package com.example.neurotrack.ui.screens.calendar.model

import java.time.LocalDate
import java.time.YearMonth
import com.example.neurotrack.data.model.Schedule

data class CalendarUiState(
    val currentMonth: YearMonth = YearMonth.now(),
    val selectedDate: LocalDate? = null,
    val events: Map<LocalDate, List<Schedule>> = emptyMap(),
    val isLoading: Boolean = false
) 