package com.example.neurotrack.data.dao

import androidx.room.*
import com.example.neurotrack.data.model.BehaviorRecord

@Dao
interface BehaviorRecordDao {
    @Query("SELECT * FROM behavior_records")
    suspend fun getAllBehaviorRecords(): List<BehaviorRecord>

    @Query("SELECT * FROM behavior_records WHERE id = :id")
    suspend fun getBehaviorRecordById(id: Int): BehaviorRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBehaviorRecord(behaviorRecord: BehaviorRecord)

    @Update
    suspend fun updateBehaviorRecord(behaviorRecord: BehaviorRecord)

    @Delete
    suspend fun deleteBehaviorRecord(behaviorRecord: BehaviorRecord)
} 