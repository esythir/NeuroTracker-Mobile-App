package com.example.neurotrack.data.local.dao

import androidx.room.*
import com.example.neurotrack.data.local.entity.BehaviorRecord
import com.example.neurotrack.data.local.entity.BehaviorRecordWithBehavior
import kotlinx.coroutines.flow.Flow

@Dao
interface BehaviorRecordDao {
    @Query("SELECT * FROM behavior_records ORDER BY timestamp DESC")
    fun getAllBehaviorRecords(): Flow<List<BehaviorRecord>>

    @Query("SELECT * FROM behavior_records WHERE id = :id")
    suspend fun getBehaviorRecordById(id: Int): BehaviorRecord?

    @Query("SELECT * FROM behavior_records WHERE behaviorId = :behaviorId ORDER BY timestamp DESC")
    fun getBehaviorRecordsForBehavior(behaviorId: Int): Flow<List<BehaviorRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBehaviorRecord(behaviorRecord: BehaviorRecord)

    @Update
    suspend fun updateBehaviorRecord(behaviorRecord: BehaviorRecord)

    @Delete
    suspend fun deleteBehaviorRecord(behaviorRecord: BehaviorRecord)

    @Query("""
        SELECT * FROM behavior_records 
        WHERE timestamp >= :startTime AND timestamp < :endTime
        ORDER BY timestamp DESC
    """)
    fun getBehaviorRecordsBetweenDates(startTime: Long, endTime: Long): Flow<List<BehaviorRecord>>

    @Query("SELECT * FROM behavior_records WHERE timestamp >= :timestamp ORDER BY timestamp DESC")
    suspend fun getBehaviorRecordsAfterDate(timestamp: Long): List<BehaviorRecord>

    @Query("DELETE FROM behavior_records")
    suspend fun deleteAllBehaviorRecords()

    @Query("""
        SELECT br.*, b.name AS behaviorName
        FROM behavior_records AS br
        INNER JOIN behaviors AS b ON b.id = br.behaviorId
        ORDER BY br.timestamp DESC
    """)
    fun getAllBehaviorRecordsWithBehaviorName(): Flow<List<BehaviorRecordWithBehavior>>

    @Query("""
        SELECT br.*, b.name AS behaviorName
        FROM behavior_records AS br
        INNER JOIN behaviors AS b ON b.id = br.behaviorId
        WHERE br.timestamp >= :startTime AND br.timestamp < :endTime
        ORDER BY br.timestamp DESC
    """)
    fun getBehaviorRecordsBetweenDatesWithBehaviorName(
        startTime: Long,
        endTime: Long
    ): Flow<List<BehaviorRecordWithBehavior>>
}
