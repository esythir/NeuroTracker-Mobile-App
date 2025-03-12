package com.example.neurotrack.data.local.dao

import androidx.room.*
import com.example.neurotrack.data.local.entity.Behavior
import kotlinx.coroutines.flow.Flow

@Dao
interface BehaviorDao {
    @Query("SELECT * FROM behaviors ORDER BY name ASC")
    fun getAllBehaviors(): Flow<List<Behavior>>
    
    @Query("SELECT * FROM behaviors WHERE id = :id")
    suspend fun getBehaviorById(id: Int): Behavior?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBehavior(behavior: Behavior)
    
    @Update
    suspend fun updateBehavior(behavior: Behavior)
    
    @Delete
    suspend fun deleteBehavior(behavior: Behavior)
} 