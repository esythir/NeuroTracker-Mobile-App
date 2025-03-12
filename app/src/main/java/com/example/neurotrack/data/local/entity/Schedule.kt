package com.example.neurotrack.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.neurotrack.data.local.entity.Behavior

@Entity(
    tableName = "schedules",
    foreignKeys = [
        ForeignKey(
            entity = Behavior::class,
            parentColumns = ["id"],
            childColumns = ["behaviorId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("behaviorId")]
)
data class Schedule(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val behaviorId: Int,
    val time: Long,                    // Horário programado
    val isRecurring: Boolean = false,
    val recurringDays: String? = null,
    val isEnabled: Boolean = true
)

enum class RepeatType {
    ONCE,       // Uma vez
    DAILY,      // Todos os dias
    WEEKLY,     // Semanalmente
    MONTHLY     // Mensalmente
} 