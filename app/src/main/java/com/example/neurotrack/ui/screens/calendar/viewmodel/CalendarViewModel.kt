package com.example.neurotrack.ui.screens.calendar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neurotrack.data.local.dao.BehaviorRecordDao
import com.example.neurotrack.data.local.entity.BehaviorRecord
import com.example.neurotrack.ui.components.Record
import com.example.neurotrack.ui.screens.calendar.model.CalendarUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.ZoneId

class CalendarViewModel(
    private val behaviorRecordDao: BehaviorRecordDao
) : ViewModel() {
    private val _state = MutableStateFlow(CalendarUiState())
    val state: StateFlow<CalendarUiState> = _state.asStateFlow()

    init {
        loadCurrentMonth()
    }

    fun updateMonth(month: YearMonth) {
        _state.update { it.copy(currentMonth = month) }
        loadMarkedDates(month)
    }

    fun selectDate(date: LocalDate) {
        _state.update { it.copy(selectedDate = date) }
        loadRecordsForDate(date)
    }

    private fun loadCurrentMonth() {
        val currentMonth = YearMonth.now()
        _state.update { it.copy(currentMonth = currentMonth) }
        loadMarkedDates(currentMonth)
    }

    private fun loadMarkedDates(month: YearMonth) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            try {
                val startOfMonth = month.atDay(1).atStartOfDay()
                    .atZone(ZoneId.systemDefault())
                    .toEpochSecond()
                val endOfMonth = month.atEndOfMonth().plusDays(1).atStartOfDay()
                    .atZone(ZoneId.systemDefault())
                    .toEpochSecond()

                behaviorRecordDao.getBehaviorRecordsBetweenDates(startOfMonth, endOfMonth)
                    .collect { records ->
                        val markedDates = records.map { record ->
                            Instant.ofEpochSecond(record.timestamp)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        }.toSet()

                        _state.update { it.copy(markedDates = markedDates) }
                    }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun loadRecordsForDate(date: LocalDate) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            try {
                val startOfDay = date.atStartOfDay()
                    .atZone(ZoneId.systemDefault())
                    .toEpochSecond()
                val endOfDay = date.plusDays(1).atStartOfDay()
                    .atZone(ZoneId.systemDefault())
                    .toEpochSecond()

                behaviorRecordDao.getBehaviorRecordsBetweenDates(startOfDay, endOfDay)
                    .collect { records ->
                        val mappedRecords = records.map { record ->
                            Record(
                                id = record.id.toLong(),
                                title = record.mood ?: "Sem humor registrado",
                                description = record.notes ?: "Sem observações",
                                timestamp = LocalDateTime.ofInstant(
                                    Instant.ofEpochSecond(record.timestamp),
                                    ZoneId.systemDefault()
                                ),
                                score = record.intensity
                            )
                        }
                        _state.update { it.copy(records = mappedRecords) }
                    }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
} 