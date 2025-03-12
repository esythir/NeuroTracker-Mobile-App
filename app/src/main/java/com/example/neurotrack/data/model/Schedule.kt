package com.example.neurotrack.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

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
    val time: Long,
    val isRecurring: Boolean = false,
    val recurringDays: String? = null, // Dias da semana em formato "1,2,3,4,5" (1=domingo, 7=sábado)
    val isEnabled: Boolean = true
) 