package com.example.neurotrack.ui.screens.calendar.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neurotrack.data.local.dao.BehaviorRecordDao
import com.example.neurotrack.ui.components.Record
import com.example.neurotrack.ui.screens.calendar.model.CalendarUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZoneOffset

@RequiresApi(Build.VERSION_CODES.O)
class CalendarViewModel(
    private val behaviorRecordDao: BehaviorRecordDao
) : ViewModel() {
    private val _state = MutableStateFlow(CalendarUiState())
    val state: StateFlow<CalendarUiState> = _state.asStateFlow()

    init {
        loadCurrentMonth()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateMonth(month: YearMonth) {
        _state.update { it.copy(currentMonth = month) }
        loadMarkedDates(month)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun selectDate(date: LocalDate) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    selectedDate = date,
                    isLoading = true,
                    records = emptyList()
                )
            }

            try {
                val startOfDay = date.atStartOfDay().toInstant(ZoneOffset.UTC).epochSecond
                val endOfDay = date.atTime(23, 59, 59).toInstant(ZoneOffset.UTC).epochSecond

                behaviorRecordDao.getBehaviorRecordsBetweenDatesWithBehaviorName(startOfDay, endOfDay)
                    .collect { recordsWithBehavior ->
                        val mappedRecords = recordsWithBehavior.map { item ->
                            val r = item.behaviorRecord
                            Record(
                                id = r.id.toLong(),
                                title = item.behaviorName,
                                description = r.notes ?: "Sem observações",
                                timestamp = LocalDateTime.ofInstant(
                                    Instant.ofEpochSecond(r.timestamp),
                                    ZoneId.systemDefault()
                                ),
                                score = r.intensity,
                                mood = r.mood
                            )
                        }
                        _state.update { it.copy(records = mappedRecords, isLoading = false) }
                    }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        records = emptyList()
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadCurrentMonth() {
        val currentMonth = YearMonth.now()
        _state.update { it.copy(currentMonth = currentMonth) }
        loadMarkedDates(currentMonth)
    }

    @RequiresApi(Build.VERSION_CODES.O)
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

                behaviorRecordDao.getBehaviorRecordsBetweenDatesWithBehaviorName(startOfMonth, endOfMonth)
                    .collect { recordsWithBehavior ->
                        val markedDates = recordsWithBehavior.map { item ->
                            Instant.ofEpochSecond(item.behaviorRecord.timestamp)
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
}
