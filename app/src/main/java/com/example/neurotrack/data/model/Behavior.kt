package com.example.neurotrack.data.model

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