package com.example.neurotrack.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neurotrack.data.local.dao.BehaviorRecordDao
import com.example.neurotrack.ui.components.Record
import kotlinx.coroutines.flow.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

class HistoryViewModel(
    private val behaviorRecordDao: BehaviorRecordDao
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryState())
    val state: StateFlow<HistoryState> = _state.asStateFlow()

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
                                isRefreshing = false
                            )
                        }
                    }
            } catch (e: Exception) {
                _state.update { it.copy(isRefreshing = false) }
            }
        }
    }

    fun onRecordClick(record: Record) {
        // TODO: Implementar navegação para detalhes do registro
    }
}

data class HistoryState(
    val records: List<Record> = emptyList(),
    val isRefreshing: Boolean = false
) 