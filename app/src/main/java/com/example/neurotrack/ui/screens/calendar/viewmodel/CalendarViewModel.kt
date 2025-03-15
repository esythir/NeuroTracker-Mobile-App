package com.example.neurotrack.ui.screens.calendar.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neurotrack.data.repository.BehaviorRecordRepository
import com.example.neurotrack.data.repository.ScheduleRepository
import com.example.neurotrack.ui.screens.calendar.model.CalendarUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import kotlin.random.Random

data class CalendarState @RequiresApi(Build.VERSION_CODES.O) constructor(
    val currentMonth: YearMonth = YearMonth.now(),
    val selectedDate: LocalDate? = null,
    val today: LocalDate = LocalDate.now(),
    val markedDates: Set<LocalDate> = generateInitialMarkedDates()
)

private fun generateInitialMarkedDates(): Set<LocalDate> {
    val today = LocalDate.now()
    val dates = mutableSetOf<LocalDate>()
    
    // Exemplo: marcar alguns dias aleatórios nos últimos 6 meses
    for (i in 0..180) {
        if (Random.nextFloat() > 0.7f) { // 30% de chance de marcar o dia
            dates.add(today.minusDays(i.toLong()))
        }
    }
    return dates
}

class CalendarViewModel : ViewModel() {
    private val _state = MutableStateFlow(CalendarState())
    val state: StateFlow<CalendarState> = _state.asStateFlow()

    init {
        loadCalendarData()
        // Exemplo de datas marcadas (você pode substituir por suas próprias datas)
        val currentMonth = YearMonth.now()
        val markedDates = setOf(
            currentMonth.atDay(1),
            currentMonth.atDay(2),
            currentMonth.atDay(3),
            currentMonth.atDay(4),
            currentMonth.atDay(5),
            currentMonth.atDay(31),
            currentMonth.atDay(30),
            currentMonth.atDay(29),
            currentMonth.atDay(28),
            currentMonth.atDay(27),
            currentMonth.atDay(26)
        )
        _state.update { it.copy(markedDates = markedDates) }
    }

    private fun loadCalendarData() {
        viewModelScope.launch {
            // TODO: Carregar dados do calendário
        }
    }

    fun updateMonth(month: YearMonth) {
        _state.update { it.copy(currentMonth = month) }
    }

    fun selectDate(date: LocalDate) {
        _state.update { it.copy(selectedDate = date) }
    }

    fun toggleDateMark(date: LocalDate) {
        _state.update { currentState ->
            val newMarkedDates = currentState.markedDates.toMutableSet()
            if (date in newMarkedDates) {
                newMarkedDates.remove(date)
            } else {
                newMarkedDates.add(date)
            }
            currentState.copy(markedDates = newMarkedDates)
        }
    }
} 