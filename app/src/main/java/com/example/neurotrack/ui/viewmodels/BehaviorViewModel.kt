package com.example.neurotrack.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.neurotrack.data.repository.BehaviorRepository

class BehaviorViewModel(
    private val repository: BehaviorRepository
) : ViewModel() {
    suspend fun getAllBehaviors() = repository.getAllBehaviors()
    suspend fun getBehaviorById(id: Int) = repository.getBehaviorById(id)
} 