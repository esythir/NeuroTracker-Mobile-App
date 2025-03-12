package com.example.neurotrack.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "behaviors")
data class Behavior(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String? = null,
    val type: String
)

enum class BehaviorType {
    POSITIVE,    // Comportamentos que queremos aumentar
    NEGATIVE     // Comportamentos que queremos diminuir
} 