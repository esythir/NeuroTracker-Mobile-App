package com.example.neurotrack.data.dao

import androidx.room.*
import com.example.neurotrack.data.model.Behavior

@Dao
interface BehaviorDao {
    @Query("SELECT * FROM behaviors")
    suspend fun getAllBehaviors(): List<Behavior>

    @Query("SELECT * FROM behaviors WHERE id = :id")
    suspend fun getBehaviorById(id: Int): Behavior?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBehavior(behavior: Behavior)

    @Update
    suspend fun updateBehavior(behavior: Behavior)

    @Delete
    suspend fun deleteBehavior(behavior: Behavior)
} 