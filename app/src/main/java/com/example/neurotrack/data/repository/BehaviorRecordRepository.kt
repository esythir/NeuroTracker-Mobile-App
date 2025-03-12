package com.example.neurotrack.data.repository

import com.example.neurotrack.data.local.dao.BehaviorRecordDao
import com.example.neurotrack.data.local.entity.BehaviorRecord
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent

class BehaviorRecordRepository(
    private val behaviorRecordDao: BehaviorRecordDao
) : KoinComponent {
    fun getAllBehaviorRecords(): Flow<List<BehaviorRecord>> = behaviorRecordDao.getAllBehaviorRecords()
    
    suspend fun getBehaviorRecordById(id: Int) = behaviorRecordDao.getBehaviorRecordById(id)
    
    fun getBehaviorRecordsForBehavior(behaviorId: Int): Flow<List<BehaviorRecord>> = 
        behaviorRecordDao.getBehaviorRecordsForBehavior(behaviorId)
    
    suspend fun insertBehaviorRecord(behaviorRecord: BehaviorRecord) = 
        behaviorRecordDao.insertBehaviorRecord(behaviorRecord)
    
    suspend fun updateBehaviorRecord(behaviorRecord: BehaviorRecord) = 
        behaviorRecordDao.updateBehaviorRecord(behaviorRecord)
    
    suspend fun deleteBehaviorRecord(behaviorRecord: BehaviorRecord) = 
        behaviorRecordDao.deleteBehaviorRecord(behaviorRecord)
} 