package com.example.neurotrack.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.neurotrack.data.repository.BehaviorRecordRepository

class BehaviorRecordViewModel(
    private val repository: BehaviorRecordRepository
) : ViewModel() {
    suspend fun getAllBehaviorRecords() = repository.getAllBehaviorRecords()
    suspend fun getBehaviorRecordById(id: Int) = repository.getBehaviorRecordById(id)
} 