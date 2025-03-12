package com.example.neurotrack.data.dao

import androidx.room.*
import com.example.neurotrack.data.model.Schedule

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedules")
    suspend fun getAllSchedules(): List<Schedule>

    @Query("SELECT * FROM schedules WHERE id = :id")
    suspend fun getScheduleById(id: Int): Schedule?

    @Query("SELECT * FROM schedules WHERE behaviorId = :behaviorId")
    suspend fun getSchedulesForBehavior(behaviorId: Int): List<Schedule>

    @Query("SELECT * FROM schedules WHERE isEnabled = 1")
    suspend fun getActiveSchedules(): List<Schedule>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: Schedule)

    @Update
    suspend fun updateSchedule(schedule: Schedule)

    @Delete
    suspend fun deleteSchedule(schedule: Schedule)
} 