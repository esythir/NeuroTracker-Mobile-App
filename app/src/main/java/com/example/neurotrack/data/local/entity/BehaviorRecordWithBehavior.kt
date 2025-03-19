package com.example.neurotrack.data.local.entity

import androidx.room.Embedded

data class BehaviorRecordWithBehavior(
    @Embedded
    val behaviorRecord: BehaviorRecord,
    val behaviorName: String
)
