package com.example.neurotrack.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.neurotrack.data.local.entity.Behavior

@Entity(
    tableName = "behavior_records",
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
data class BehaviorRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val behaviorId: Int,
    val timestamp: Long,
    val mood: String?,               // Humor geral (Péssimo, Mal, Okay, Bem, Ótimo)
    val feelings: String?,           // Lista de sentimentos como String
    val intensity: Int,              // 1-5
    val duration: String,            // "15 min", "30 min", etc
    val trigger: String?,            // Gatilho do comportamento
    val notes: String? = null        // Observações adicionais
) 