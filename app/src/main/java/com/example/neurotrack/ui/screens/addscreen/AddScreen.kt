package com.example.neurotrack.ui.screens.addscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*
import org.koin.androidx.compose.koinViewModel
import com.example.neurotrack.data.local.entity.Feeling
import com.example.neurotrack.ui.model.UiFeeling
import androidx.compose.ui.layout.Layout
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle

@Composable
fun AddScreen(
    viewModel: AddViewModel = koinViewModel()
) {
    var showSuccessDialog by remember { mutableStateOf(false) }
    var selectedMood by remember { mutableStateOf<String?>(null) }
    val selectedFeelings = remember { mutableStateListOf<UiFeeling>() }
    var selectedIntensity by remember { mutableStateOf<Int?>(null) }
    val behaviorOptions = listOf("Seletividade Alimentar", "Crise Emocional", "Hiperatividade")
    val durationOptions = listOf("15 min", "30 min", "45 min", "1h", "1h30", "2h", "2h+")
    val triggerOptions = listOf("Sons altos", "Mudança brusca de rotina", "Fome", "Luz intensa", "Outro")
    var selectedBehavior by remember { mutableStateOf("") }
    var selectedDuration by remember { mutableStateOf("") }
    var selectedTrigger by remember { mutableStateOf("") }

    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
    var dateTime by remember { mutableStateOf(dateFormat.format(Date())) }

    var observations by remember { mutableStateOf("") }

    val allFeelings = listOf(
        UiFeeling("Medo", Color(0xFFFFE5E5)),
        UiFeeling("Angústia", Color(0xFFFFE5E5)),
        UiFeeling("Ansiedade", Color(0xFFFFF2E5)),
        UiFeeling("Triste", Color(0xFFFFF2E5)),
        UiFeeling("Irritado", Color(0xFFFFF2E5)),
        UiFeeling("Estressado", Color(0xFFFFF2E5)),
        UiFeeling("Sonolento", Color(0xFFE5EEFF)),
        UiFeeling("Cansado", Color(0xFFE5EEFF)),
        UiFeeling("Confuso", Color(0xFFE5EEFF)),
        UiFeeling("Entediado", Color(0xFFE5FFE8)),
        UiFeeling("Hiperfocado", Color(0xFFE5FFE8)),
        UiFeeling("Pensativo", Color(0xFFE5FFE8)),
        UiFeeling("Animado", Color(0xFFE5FFF8)),
        UiFeeling("Feliz", Color(0xFFE5FFF8)),
        UiFeeling("Confiante", Color(0xFFE5FFF8)),
        UiFeeling("Grato", Color(0xFFE5FFF8)),
        UiFeeling("Inspirado", Color(0xFFE5FFF8)),
        UiFeeling("Tranquilo", Color(0xFFE5FFF8)),
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        color = Color(0xFFF5F5F5)
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .shadow(10.dp, RoundedCornerShape(15.dp))
                .background(Color.White, RoundedCornerShape(15.dp))
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 30.dp)
                ) {
                    Text(
                        text = "←",
                        fontSize = 24.sp,
                        modifier = Modifier
                            .clickable { /* navigate back */ }
                            .padding(end = 10.dp),
                        color = Color(0xFF333333)
                    )
                    Text(
                        text = "Registre Novo Humor",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f),
                        color = Color(0xFF333333)
                    )
                    Text(
                        text = "ⓘ",
                        fontSize = 20.sp,
                        color = Color(0xFF999999)
                    )
                }

                Text("Como você está se sentindo hoje?", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text(
                    "Toque em um emoji para selecionar seu humor.",
                    fontSize = 13.sp,
                    color = Color(0xFF666666),
                    modifier = Modifier.padding(bottom = 15.dp, top = 0.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    EmojiButton("😢", "Péssimo", selectedMood) { selectedMood = it }
                    EmojiButton("😕", "Mal", selectedMood) { selectedMood = it }
                    EmojiButton("😐", "Okay", selectedMood) { selectedMood = it }
                    EmojiButton("🙂", "Bem", selectedMood) { selectedMood = it }
                    EmojiButton("😄", "Ótimo", selectedMood) { selectedMood = it }
                }
                Spacer(modifier = Modifier.height(40.dp))

                Text("Sentimentos Envolvidos", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text(
                    "Selecione os sentimentos relacionados ao seu humor.",
                    fontSize = 13.sp,
                    color = Color(0xFF666666),
                    modifier = Modifier.padding(bottom = 15.dp)
                )
                MultiSelectDropdown(
                    allOptions = allFeelings,
                    selectedOptions = selectedFeelings
                )
                Spacer(modifier = Modifier.height(40.dp))

                Text("Nome do Comportamento", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text("Selecione o comportamento predominante.", fontSize = 13.sp, color = Color(0xFF666666))
                SingleSelectDropdown(
                    modifier = Modifier.padding(top = 8.dp),
                    options = behaviorOptions,
                    selectedOption = selectedBehavior,
                    onSelect = { selectedBehavior = it },
                    placeholder = "Selecione o comportamento"
                )
                Spacer(modifier = Modifier.height(40.dp))

                Text("Duração do Comportamento", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text("Selecione a duração em minutos.", fontSize = 13.sp, color = Color(0xFF666666))
                SingleSelectDropdown(
                    modifier = Modifier.padding(top = 8.dp),
                    options = durationOptions,
                    selectedOption = selectedDuration,
                    onSelect = { selectedDuration = it },
                    placeholder = "Selecione a duração"
                )
                Spacer(modifier = Modifier.height(40.dp))

                Text("Intensidade do Comportamento", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text("Selecione a intensidade de 1 a 5.", fontSize = 13.sp, color = Color(0xFF666666))
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    (1..5).forEach { num ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .border(
                                    1.dp,
                                    Color.LightGray,
                                    shape = CircleShape
                                )
                                .background(
                                    if (selectedIntensity == num) Color(0xFFFFD700)
                                    else Color.Transparent,
                                    CircleShape
                                )
                                .clickable {
                                    selectedIntensity = num
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = num.toString(), fontSize = 16.sp)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))

                Text("Gatilho do Comportamento", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text("Selecione o gatilho.", fontSize = 13.sp, color = Color(0xFF666666))
                SingleSelectDropdown(
                    modifier = Modifier.padding(top = 8.dp),
                    options = triggerOptions,
                    selectedOption = selectedTrigger,
                    onSelect = { selectedTrigger = it },
                    placeholder = "Selecione o gatilho"
                )
                Spacer(modifier = Modifier.height(40.dp))

                Text("Data e Hora do Ocorrido", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text("Selecione a data e hora.", fontSize = 13.sp, color = Color(0xFF666666))
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = dateTime,
                    onValueChange = { dateTime = it },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
                )
                Spacer(modifier = Modifier.height(40.dp))

                Text("Observações", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text("Adicione observações sobre o comportamento.", fontSize = 13.sp, color = Color(0xFF666666))
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = observations,
                    onValueChange = { observations = it },
                    placeholder = { Text("Detalhes adicionais...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
                )
                Spacer(modifier = Modifier.height(25.dp))

                Button(
                    onClick = {
                        viewModel.saveBehaviorRecord(
                            behaviorId = 1,
                            mood = selectedMood,
                            feelings = selectedFeelings.map { it.text },
                            intensity = selectedIntensity ?: 1,
                            duration = selectedDuration,
                            trigger = selectedTrigger,
                            notes = observations
                        )
                        showSuccessDialog = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text("Registrar Humor", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.height(15.dp))
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Cancelar",
                        fontSize = 14.sp,
                        color = Color(0xFF666666),
                        modifier = Modifier.clickable {
                            // Handle cancel
                        }
                    )
                }
            }
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            icon = {
                Icon(
                    Icons.Filled.CheckCircle,
                    contentDescription = "Sucesso",
                    tint = Color.Green,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text("Sucesso!")
            },
            text = {
                Text("Seu registro foi salvo com sucesso.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSuccessDialog = false
                        selectedMood = null
                        selectedFeelings.clear()
                        selectedIntensity = null
                        selectedDuration = ""
                        selectedTrigger = ""
                        observations = ""
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}

data class Feeling(val text: String, val color: Color)

@Composable
fun EmojiButton(emoji: String, label: String, selectedMood: String?, onSelected: (String) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable {
            onSelected(label)
        }
    ) {
        val bgColor =
            if (selectedMood == label) Color(0xFFFFD700)
            else Color(0xFFF0F0F0)
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(bgColor, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = emoji, fontSize = 32.sp)
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = label, fontSize = 12.sp, color = Color(0xFF666666))
    }
}

@Composable
fun MultiSelectDropdown(
    allOptions: List<UiFeeling>,
    selectedOptions: MutableList<UiFeeling>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFCCCCCC), RoundedCornerShape(5.dp))
                .clickable { expanded = true }
                .padding(16.dp)
        ) {
            if (selectedOptions.isEmpty()) {
                Text(
                    text = "Selecione os sentimentos",
                    color = Color(0xFFAAAAAA)
                )
            } else {
                Text(
                    text = selectedOptions.joinToString(", ") { it.text },
                    color = Color.Black
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth(0.5f)  // 50% da largura da tela
                .heightIn(max = 300.dp)  // Altura máxima
        ) {
            allOptions.forEach { feeling ->
                DropdownMenuItem(
                    text = { Text(feeling.text) },
                    onClick = {
                        if (feeling !in selectedOptions) {
                            selectedOptions.add(feeling)
                        }
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }
        
        layout(
            width = constraints.maxWidth,
            height = constraints.maxHeight
        ) {
            var xPosition = 0
            var yPosition = 0
            var maxHeight = 0

            placeables.forEach { placeable ->
                if (xPosition + placeable.width > constraints.maxWidth) {
                    xPosition = 0
                    yPosition += maxHeight
                    maxHeight = 0
                }
                placeable.place(xPosition, yPosition)
                xPosition += placeable.width
                maxHeight = maxOf(maxHeight, placeable.height)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Chip(
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.padding(8.dp)
        ) {
            label()
        }
    }
}

@Composable
fun SingleSelectDropdown(
    modifier: Modifier = Modifier,
    options: List<String>,
    selectedOption: String,
    onSelect: (String) -> Unit,
    placeholder: String
) {
    var dropdownExpanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFCCCCCC), RoundedCornerShape(5.dp))
                    .padding(8.dp)
                    .clickable {
                        focusManager.clearFocus()
                        dropdownExpanded = !dropdownExpanded
                    }
            ) {
                if (selectedOption.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = Color(0xFFAAAAAA),
                        fontSize = 14.sp
                    )
                } else {
                    Text(
                        text = selectedOption,
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                }
            }

            DropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onSelect(option)
                            dropdownExpanded = false
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun AddScreenPreview() {
    MaterialTheme {
        AddScreen()
    }
}