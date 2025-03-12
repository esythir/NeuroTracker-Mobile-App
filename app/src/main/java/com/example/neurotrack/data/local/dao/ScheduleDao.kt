package com.example.neurotrack.data.local.dao

import androidx.room.*
import com.example.neurotrack.data.local.entity.Schedule
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedules ORDER BY time ASC")
    fun getAllSchedules(): Flow<List<Schedule>>

    @Query("SELECT * FROM schedules WHERE id = :id")
    suspend fun getScheduleById(id: Int): Schedule?

    @Query("SELECT * FROM schedules WHERE behaviorId = :behaviorId ORDER BY time ASC")
    fun getSchedulesForBehavior(behaviorId: Int): Flow<List<Schedule>>

    @Query("SELECT * FROM schedules WHERE isEnabled = 1 ORDER BY time ASC")
    fun getActiveSchedules(): Flow<List<Schedule>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: Schedule)

    @Update
    suspend fun updateSchedule(schedule: Schedule)

    @Delete
    suspend fun deleteSchedule(schedule: Schedule)
} 