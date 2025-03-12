package com.example.neurotrack.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.neurotrack.data.dao.BehaviorDao
import com.example.neurotrack.data.dao.BehaviorRecordDao
import com.example.neurotrack.data.dao.ScheduleDao
import com.example.neurotrack.data.model.Behavior
import com.example.neurotrack.data.model.BehaviorRecord
import com.example.neurotrack.data.model.Schedule

@Database(
    entities = [
        Behavior::class,
        BehaviorRecord::class,
        Schedule::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun behaviorDao(): BehaviorDao
    abstract fun behaviorRecordDao(): BehaviorRecordDao
    abstract fun scheduleDao(): ScheduleDao
} 