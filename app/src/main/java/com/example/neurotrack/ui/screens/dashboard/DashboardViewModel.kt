package com.example.neurotrack.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neurotrack.data.local.dao.BehaviorRecordDao
import com.example.neurotrack.data.service.GeminiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset

class DashboardViewModel(
    private val behaviorRecordDao: BehaviorRecordDao,
    private val geminiService: GeminiService
) : ViewModel() {
    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val thirtyDaysAgo = LocalDateTime.now().minusDays(30)
                val records = behaviorRecordDao.getBehaviorRecordsAfterDate(
                    thirtyDaysAgo.toEpochSecond(ZoneOffset.UTC)
                )
                
                // Análise com IA
                val analysis = geminiService.analyzePatterns(records)
                
                _state.value = _state.value.copy(
                    isLoading = false,
                    totalRecords = records.size,
                    mainInsight = analysis.mainInsight,
                    patterns = analysis.patterns,
                    triggers = analysis.triggers,
                    recommendations = analysis.recommendations
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Erro ao carregar dados: ${e.message}"
                )
            }
        }
    }
}

data class DashboardState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val totalRecords: Int = 0,
    val mainInsight: String = "",
    val patterns: List<String> = emptyList(),
    val triggers: List<String> = emptyList(),
    val recommendations: List<String> = emptyList()
) 