package com.example.neurotrack.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.neurotrack.data.repository.ScheduleRepository

class ScheduleViewModel(
    private val repository: ScheduleRepository
) : ViewModel() {
    suspend fun getAllSchedules() = repository.getAllSchedules()
    suspend fun getActiveSchedules() = repository.getActiveSchedules()
    suspend fun getSchedulesForBehavior(behaviorId: Int) = repository.getSchedulesForBehavior(behaviorId)
} 