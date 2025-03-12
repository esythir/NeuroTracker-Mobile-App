package com.example.neurotrack.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.neurotrack.data.local.dao.BehaviorDao
import com.example.neurotrack.data.local.dao.BehaviorRecordDao
import com.example.neurotrack.data.local.dao.ScheduleDao
import com.example.neurotrack.data.local.entity.Behavior
import com.example.neurotrack.data.local.entity.BehaviorRecord
import com.example.neurotrack.data.local.entity.Schedule

@Database(
    entities = [
        Behavior::class,
        BehaviorRecord::class,
        Schedule::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun behaviorDao(): BehaviorDao
    abstract fun behaviorRecordDao(): BehaviorRecordDao
    abstract fun scheduleDao(): ScheduleDao
} 