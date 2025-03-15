package com.example.neurotrack.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.neurotrack.data.local.dao.BehaviorDao
import com.example.neurotrack.data.local.dao.BehaviorRecordDao
import com.example.neurotrack.data.local.dao.ScheduleDao
import com.example.neurotrack.data.local.entity.Behavior
import com.example.neurotrack.data.local.entity.BehaviorRecord
import com.example.neurotrack.data.local.entity.Schedule
import com.example.neurotrack.data.local.converter.Converters
import com.example.neurotrack.data.local.entity.BehaviorType

@Database(
    entities = [
        Behavior::class,
        BehaviorRecord::class,
        Schedule::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun behaviorDao(): BehaviorDao
    abstract fun behaviorRecordDao(): BehaviorRecordDao
    abstract fun scheduleDao(): ScheduleDao

    companion object {
        private val PREPOPULATE_BEHAVIORS = listOf(
            Behavior(
                id = 1,
                name = "Crise Emocional",
                description = "Momentos de crise emocional",
                type = BehaviorType.NEGATIVE.name
            ),
            Behavior(
                id = 2,
                name = "Seletividade Alimentar",
                description = "Comportamentos relacionados à alimentação",
                type = BehaviorType.NEGATIVE.name
            )
        )
    }
} 