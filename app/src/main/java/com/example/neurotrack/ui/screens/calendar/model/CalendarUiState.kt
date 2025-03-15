package com.example.neurotrack.ui.screens.calendar.model

import com.example.neurotrack.ui.components.Record
import java.time.LocalDate
import java.time.YearMonth

data class CalendarUiState(
    val currentMonth: YearMonth = YearMonth.now(),
    val selectedDate: LocalDate? = null,
    val records: List<Record> = emptyList(),
    val markedDates: Set<LocalDate> = emptySet(),
    val isLoading: Boolean = false
) 