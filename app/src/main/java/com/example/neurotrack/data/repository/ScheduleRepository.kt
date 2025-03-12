package com.example.neurotrack.data.repository

import com.example.neurotrack.data.local.dao.ScheduleDao
import com.example.neurotrack.data.local.entity.Schedule
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent

class ScheduleRepository(
    private val scheduleDao: ScheduleDao
) : KoinComponent {
    fun getAllSchedules(): Flow<List<Schedule>> = scheduleDao.getAllSchedules()
    
    suspend fun getScheduleById(id: Int) = scheduleDao.getScheduleById(id)
    
    fun getSchedulesForBehavior(behaviorId: Int): Flow<List<Schedule>> = 
        scheduleDao.getSchedulesForBehavior(behaviorId)
    
    fun getActiveSchedules(): Flow<List<Schedule>> = scheduleDao.getActiveSchedules()
    
    suspend fun insertSchedule(schedule: Schedule) = scheduleDao.insertSchedule(schedule)
    
    suspend fun updateSchedule(schedule: Schedule) = scheduleDao.updateSchedule(schedule)
    
    suspend fun deleteSchedule(schedule: Schedule) = scheduleDao.deleteSchedule(schedule)
} 