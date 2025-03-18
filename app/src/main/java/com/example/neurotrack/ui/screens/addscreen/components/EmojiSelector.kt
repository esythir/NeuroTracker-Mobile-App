package com.example.neurotrack.ui.screens.addscreen.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EmojiSelector(
    emojis: List<Pair<String, String>>,
    selectedMood: String?,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        emojis.forEach { (emoji, label) ->
            EmojiButton(
                emoji = emoji,
                label = label,
                selectedMood = selectedMood,
                onSelected = onSelected
            )
        }
    }
}

@Composable
fun EmojiButton(
    emoji: String,
    label: String,
    selectedMood: String?,
    onSelected: (String) -> Unit
) {
    val isSelected = selectedMood == label
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                     else Color(0xFFF0F0F0),
        animationSpec = tween(durationMillis = 300),
        label = "backgroundColor"
    )
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onSelected(label) }
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .shadow(
                    elevation = if (isSelected) 4.dp else 1.dp,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emoji,
                fontSize = 32.sp
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isSelected) MaterialTheme.colorScheme.primary
                   else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
} 