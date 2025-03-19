package com.example.neurotrack.ui.screens.home

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

@RequiresApi(Build.VERSION_CODES.O)
class HomeViewModel(
    private val behaviorRecordDao: BehaviorRecordDao
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        loadRecords()
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
                        val totalRecords = recordsWithBehavior.size
                        val averageIntensity = if (totalRecords > 0) {
                            recordsWithBehavior.sumOf { it.behaviorRecord.intensity }.toFloat() / totalRecords
                        } else 0f

                        val moodFrequency = recordsWithBehavior
                            .groupBy { it.behaviorRecord.mood }
                            .mapValues { it.value.size }

                        val predominantMoodEntry = moodFrequency.maxByOrNull { it.value }
                        val predominantMood = predominantMoodEntry?.key ?: ""
                        val moodCount = predominantMoodEntry?.value ?: 0

                        val recordList = recordsWithBehavior.map { item ->
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
                                records = recordList,
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
                e.printStackTrace()
                _state.update { it.copy(isRefreshing = false) }
            }
        }
    }
}
