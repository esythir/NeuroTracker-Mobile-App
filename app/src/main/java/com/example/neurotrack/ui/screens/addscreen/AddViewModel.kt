package com.example.neurotrack.ui.screens.addscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neurotrack.data.local.dao.BehaviorRecordDao
import com.example.neurotrack.data.local.entity.BehaviorRecord
import com.example.neurotrack.data.local.entity.Feeling
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import kotlinx.coroutines.flow.asStateFlow
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class AddViewModel(
    private val behaviorRecordDao: BehaviorRecordDao,
    initialDate: LocalDate? = null,
    private val isFromCalendar: Boolean = false
) : ViewModel() {
    private val _selectedDate = MutableStateFlow(initialDate)
    val selectedDate: StateFlow<LocalDate?> = _selectedDate.asStateFlow()
    
    private val _showSuccessDialog = MutableStateFlow(false)
    val showSuccessDialog: StateFlow<Boolean> = _showSuccessDialog.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    val isFromCalendarFlow = MutableStateFlow(isFromCalendar)

    fun setInitialDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun saveBehaviorRecord(
        behaviorId: Int,
        mood: String?,
        feelings: List<String>,
        intensity: Int,
        duration: String,
        trigger: String?,
        notes: String?,
        dateTime: String
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
                val localDateTime = LocalDateTime.parse(dateTime, formatter)
                val timestamp = localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond()

                val record = BehaviorRecord(
                    behaviorId = behaviorId,
                    timestamp = timestamp,
                    mood = mood,
                    feelings = feelings.joinToString(","),
                    intensity = intensity,
                    duration = duration,
                    trigger = trigger,
                    notes = notes
                )
                
                behaviorRecordDao.insertBehaviorRecord(record)
                _showSuccessDialog.value = true
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun hideSuccessDialog() {
        _showSuccessDialog.value = false
    }
} 