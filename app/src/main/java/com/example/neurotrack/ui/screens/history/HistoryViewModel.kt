package com.example.neurotrack.ui.screens.history

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neurotrack.data.local.dao.BehaviorRecordDao
import com.example.neurotrack.data.local.entity.BehaviorRecordWithBehavior
import com.example.neurotrack.ui.components.Record
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadRecords() {
        viewModelScope.launch {
            try {
                behaviorRecordDao.getAllBehaviorRecordsWithBehaviorName()
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
                                score = r.intensity
                            )
                        }
                        _state.update { currentState ->
                            currentState.copy(
                                records = mappedRecords,
                                isRefreshing = false
                            )
                        }
                    }
            } catch (e: Exception) {
                _state.update { it.copy(isRefreshing = false) }
            }
        }
    }
}

data class HistoryState(
    val records: List<Record> = emptyList(),
    val isRefreshing: Boolean = false
)
