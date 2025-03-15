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

class AddViewModel(
    private val behaviorRecordDao: BehaviorRecordDao
) : ViewModel() {

    fun saveBehaviorRecord(
        behaviorId: Int,
        mood: String?,
        feelings: List<String>,
        intensity: Int,
        duration: String,
        trigger: String?,
        notes: String?
    ) {
        viewModelScope.launch {
            try {
                val record = BehaviorRecord(
                    behaviorId = behaviorId,
                    timestamp = System.currentTimeMillis() / 1000, // Convertendo para segundos
                    mood = mood,
                    feelings = feelings.joinToString(","),
                    intensity = intensity,
                    duration = duration,
                    trigger = trigger,
                    notes = notes
                )
                
                behaviorRecordDao.insertBehaviorRecord(record)
            } catch (e: Exception) {
                // Log do erro
                e.printStackTrace()
            }
        }
    }
} 