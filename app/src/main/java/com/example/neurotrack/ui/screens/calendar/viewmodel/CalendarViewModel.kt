package com.example.neurotrack.ui.screens.calendar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neurotrack.data.repository.BehaviorRecordRepository
import com.example.neurotrack.data.repository.ScheduleRepository
import com.example.neurotrack.ui.screens.calendar.model.CalendarUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CalendarViewModel(
    private val scheduleRepository: ScheduleRepository,
    private val behaviorRecordRepository: BehaviorRecordRepository
) : ViewModel() {
    private val _state = MutableStateFlow(CalendarUiState())
    val state = _state.asStateFlow()

    init {
        loadCalendarData()
    }

    private fun loadCalendarData() {
        viewModelScope.launch {
            // TODO: Carregar dados do calendário
        }
    }
} 