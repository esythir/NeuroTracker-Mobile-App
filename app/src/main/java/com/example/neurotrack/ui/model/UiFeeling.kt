package com.example.neurotrack.ui.model

import androidx.compose.ui.graphics.Color

data class UiFeeling(
    val id: Int,
    val name: String,
    val color: Color,
    val isSelected: Boolean = false
) 