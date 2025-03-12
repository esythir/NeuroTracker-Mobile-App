package com.example.neurotrack.data.repository

import com.example.neurotrack.data.local.dao.BehaviorDao
import com.example.neurotrack.data.local.entity.Behavior
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent

class BehaviorRepository(
    private val behaviorDao: BehaviorDao
) : KoinComponent {
    fun getAllBehaviors(): Flow<List<Behavior>> = behaviorDao.getAllBehaviors()
    
    suspend fun getBehaviorById(id: Int) = behaviorDao.getBehaviorById(id)
    
    suspend fun insertBehavior(behavior: Behavior) = behaviorDao.insertBehavior(behavior)
    
    suspend fun updateBehavior(behavior: Behavior) = behaviorDao.updateBehavior(behavior)
    
    suspend fun deleteBehavior(behavior: Behavior) = behaviorDao.deleteBehavior(behavior)
} 