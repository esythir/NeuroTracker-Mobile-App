package com.example.neurotrack.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neurotrack.data.local.dao.BehaviorRecordDao
import com.example.neurotrack.ui.components.Record
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

data class HomeStats(
    val totalRecords: Int = 0,
    val averageScore: Float = 0f,
    val predominantMood: String = "",
    val moodCount: Int = 0
)

data class HomeState(
    val stats: HomeStats = HomeStats(),
    val records: List<Record> = emptyList(),
    val isRefreshing: Boolean = false
)

class HomeViewModel(
    private val behaviorRecordDao: BehaviorRecordDao
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        loadRecords()
    }

    fun onRefresh() {
        _state.update { it.copy(isRefreshing = true) }
        loadRecords()
    }

    private fun loadRecords() {
        viewModelScope.launch {
            try {
                behaviorRecordDao.getAllBehaviorRecords()
                    .collect { records ->
                        val totalRecords = records.size
                        val averageIntensity = if (totalRecords > 0) {
                            records.sumOf { it.intensity }.toFloat() / totalRecords
                        } else 0f

                        // Calcula o humor predominante
                        val moodFrequency = records
                            .groupBy { it.mood }
                            .mapValues { it.value.size }
                        
                        val predominantMoodEntry = moodFrequency
                            .maxByOrNull { it.value }
                        
                        val predominantMood = predominantMoodEntry?.key ?: ""
                        val moodCount = predominantMoodEntry?.value ?: 0

                        _state.update { currentState ->
                            currentState.copy(
                                records = records.map { record ->
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
                                },
                                stats = HomeStats(
                                    totalRecords = totalRecords,
                                    averageScore = averageIntensity,
                                    predominantMood = predominantMood,
                                    moodCount = moodCount
                                ),
                                isRefreshing = false
                            )
                        }
                    }
            } catch (e: Exception) {
                println("DEBUG: Erro ao carregar registros: ${e.message}")
                e.printStackTrace()
                _state.update { it.copy(isRefreshing = false) }
            }
        }
    }

    fun onRecordClick(record: Record) {
        // TODO: Implementar navegação para detalhes do registro
    }
} 