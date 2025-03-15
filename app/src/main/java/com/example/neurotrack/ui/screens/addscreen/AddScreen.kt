package com.example.neurotrack.ui.screens.addscreen


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
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
@Composable
fun AddScreen() {
// States for user input
    var selectedMood by remember { mutableStateOf<String?>(null) }
    val selectedFeelings = remember { mutableStateListOf<Feeling>() }
    val selectedIntensity = remember { mutableStateOf<Int?>(null) }
    val behaviorOptions = listOf("Seletividade Alimentar", "Crise Emocional", "Hiperatividade")
    val durationOptions = listOf("15 min", "30 min", "45 min", "1h", "1h30", "2h", "2h+")
    val triggerOptions = listOf("Sons altos", "Mudança brusca de rotina", "Fome", "Luz intensa", "Outro")
    var selectedBehavior by remember { mutableStateOf("") }
    var selectedDuration by remember { mutableStateOf("") }
    var selectedTrigger by remember { mutableStateOf("") }

// For date/time, we'll store as string. Could also use libraries like Android datetime pickers.
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
    var dateTime by remember { mutableStateOf(dateFormat.format(Date())) }

    var observations by remember { mutableStateOf("") }

// Prepare feelings list with their color codes
    val allFeelings = listOf(
        Feeling("Medo", Color(0xFFFFE5E5)),
        Feeling("Angústia", Color(0xFFFFE5E5)),
        Feeling("Ansiedade", Color(0xFFFFF2E5)),
        Feeling("Triste", Color(0xFFFFF2E5)),
        Feeling("Irritado", Color(0xFFFFF2E5)),
        Feeling("Estressado", Color(0xFFFFF2E5)),
        Feeling("Sonolento", Color(0xFFE5EEFF)),
        Feeling("Cansado", Color(0xFFE5EEFF)),
        Feeling("Confuso", Color(0xFFE5EEFF)),
        Feeling("Entediado", Color(0xFFE5FFE8)),
        Feeling("Hiperfocado", Color(0xFFE5FFE8)),
        Feeling("Pensativo", Color(0xFFE5FFE8)),
        Feeling("Animado", Color(0xFFE5FFF8)),
        Feeling("Feliz", Color(0xFFE5FFF8)),
        Feeling("Confiante", Color(0xFFE5FFF8)),
        Feeling("Grato", Color(0xFFE5FFF8)),
        Feeling("Inspirado", Color(0xFFE5FFF8)),
        Feeling("Tranquilo", Color(0xFFE5FFF8)),
    )

// Compose UI
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        color = Color(0xFFF5F5F5)
    ) {
        // Card-like container
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
                // Header
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

                // Section: Emojis
                Text(
                    text = "Como você está se sentindo hoje?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Toque em um emoji para selecionar seu humor.",
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

                // Section: Sentimentos Envolvidos (MultiSelect)
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

                // Section: Nome do Comportamento
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

                // Section: Duração
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

                // Section: Intensidade
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
                                    if (selectedIntensity.value == num) Color(0xFFFFD700)
                                    else Color.Transparent,
                                    CircleShape
                                )
                                .clickable {
                                    selectedIntensity.value = num
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = num.toString(), fontSize = 16.sp)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))

                // Section: Gatilho
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

                // Section: Data e Hora
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

                // Section: Observações
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

                // Registrar button
                Button(
                    onClick = {
                        // Logging the data for demonstration
                        println("Selected Mood: $selectedMood")
                        println("Selected Feelings: ${selectedFeelings.map { it.text }}")
                        println("Selected Behavior: $selectedBehavior")
                        println("Selected Duration: $selectedDuration")
                        println("Selected Intensity: ${selectedIntensity.value}")
                        println("Selected Trigger: $selectedTrigger")
                        println("Date/Time: $dateTime")
                        println("Observations: $observations")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text("Registrar Humor", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }

                // Cancel link
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
}
// DATA CLASS FOR FEELINGS
data class Feeling(val text: String, val color: Color)
// EMOJI BUTTON
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
// MULTI-SELECT DROPDOWN FOR FEELINGS
@Composable
fun MultiSelectDropdown(
    allOptions: List<Feeling>,
    selectedOptions: MutableList<Feeling>
) {
    var dropdownVisible by remember { mutableStateOf(false) }
// Just show tags horizontally. If you want a more advanced layout, use FlowRow or LazyRow.
    Column {
        // Display selected tags
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFCCCCCC), RoundedCornerShape(5.dp))
                .padding(5.dp)
                .clickable { dropdownVisible = !dropdownVisible }
        ) {
            if (selectedOptions.isEmpty()) {
                Text(
                    text = "Clique para selecionar sentimentos",
                    color = Color(0xFFAAAAAA),
                    fontSize = 14.sp
                )
            } else {
                Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.wrapContentWidth()) {
                    selectedOptions.forEach { feeling ->
                        Box(
                            modifier = Modifier
                                .padding(end = 5.dp, bottom = 5.dp)
                                .background(feeling.color, RoundedCornerShape(15.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                        ) {
                            Text(
                                text = feeling.text + " ×",
                                fontSize = 13.sp,
                                modifier = Modifier.clickable {
                                    // remove if clicked on X
                                    selectedOptions.remove(feeling)
                                }
                            )
                        }
                    }
                }
            }
        }

        // Dropdown list
        DropdownMenu(
            expanded = dropdownVisible,
            onDismissRequest = { dropdownVisible = false }
        ) {
            allOptions.forEach { feeling ->
                DropdownMenuItem(
                    text = { Text(feeling.text) },
                    onClick = {
                        // Toggle selection
                        if (selectedOptions.contains(feeling)) {
                            selectedOptions.remove(feeling)
                        } else {
                            selectedOptions.add(feeling)
                        }
                    }
                )
            }
        }
    }
}
// SINGLE-SELECT DROPDOWN
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
            // Current selection
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

            // Dropdown
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
    }}