package com.example.neurotrack.ui.screens.addscreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.neurotrack.data.preferences.UserPreferencesManager
import com.example.neurotrack.ui.model.UiFeeling
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddScreen(
    initialDate: LocalDate? = null,
    isFromCalendar: Boolean = false,
    onNavigateBack: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToCalendar: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: AddViewModel = koinViewModel { parametersOf(initialDate, isFromCalendar) }
) {
    val selectedDate by viewModel.selectedDate.collectAsState()
    val showSuccessDialog by viewModel.showSuccessDialog.collectAsState()
    val isFromCalendarState by viewModel.isFromCalendarFlow.collectAsState()
    var showTimePicker by remember { mutableStateOf(false) }
    var selectedDateTime by remember {
        val now = LocalDateTime.now()
        val date = selectedDate ?: LocalDate.now()
        mutableStateOf(LocalDateTime.of(date, now.toLocalTime()))
    }

    val userPreferencesManager = koinInject<UserPreferencesManager>()
    val userName by userPreferencesManager.userName.collectAsState(initial = "")


    var selectedMood by remember { mutableStateOf<String?>(null) }
    val selectedFeelings = remember { mutableStateListOf<UiFeeling>() }
    var selectedIntensity by remember { mutableStateOf<Int?>(null) }
    var selectedBehavior by remember { mutableStateOf("") }
    var selectedDuration by remember { mutableStateOf("") }
    var selectedTrigger by remember { mutableStateOf("") }
    var observations by remember { mutableStateOf("") }

    val behaviorOptions = listOf("Seletividade Alimentar", "Crise Emocional", "Hiperatividade")
    val durationOptions = listOf("15 min", "30 min", "45 min", "1h", "1h30", "2h", "2h+")
    val triggerOptions = listOf("Sons altos", "Mudança brusca de rotina", "Fome", "Luz intensa", "Outro")


    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
    var dateTimeValue by remember { mutableStateOf(dateFormat.format(Date())) }


    val feelings = listOf(
        UiFeeling(1, "Feliz", Color(0xFF4CAF50)),
        UiFeeling(2, "Triste", Color(0xFF2196F3)),
        UiFeeling(3, "Irritado", Color(0xFFF44336)),
        UiFeeling(4, "Ansioso", Color(0xFFFF9800)),
        UiFeeling(5, "Calmo", Color(0xFF03A9F4)),
        UiFeeling(6, "Confuso", Color(0xFF9C27B0)),
        UiFeeling(7, "Entediado", Color(0xFF607D8B)),
        UiFeeling(8, "Esperançoso", Color(0xFF8BC34A)),
        UiFeeling(9, "Frustrado", Color(0xFFE91E63)),
        UiFeeling(10, "Grato", Color(0xFF009688)),
        UiFeeling(11, "Inseguro", Color(0xFFFF5722)),
        UiFeeling(12, "Motivado", Color(0xFF3F51B5)),
        UiFeeling(13, "Orgulhoso", Color(0xFFCDDC39)),
        UiFeeling(14, "Preocupado", Color(0xFF795548)),
        UiFeeling(15, "Relaxado", Color(0xFF00BCD4)),
        UiFeeling(16, "Sobrecarregado", Color(0xFF673AB7)),
        UiFeeling(17, "Solitário", Color(0xFF9E9E9E)),
        UiFeeling(18, "Surpreso", Color(0xFFFFEB3B))
    )


    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        color = Color(0xFFF5F5F5)
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
                        .clickable { onNavigateBack() }
                        .padding(end = 10.dp),
                    color = Color(0xFF333333)
                )
                Text(
                    text = "Novo Registro",
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


            Text(
                "Como $userName está se sentindo?",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Text(
                "Toque em um emoji para selecionar o humor",
                fontSize = 13.sp,
                color = Color(0xFF666666),
                modifier = Modifier.padding(bottom = 16.dp)
            )


            val emojiColors = mapOf(
                "Feliz" to Color(0xFF4CAF50),
                "Neutro" to Color(0xFF9E9E9E),
                "Triste" to Color(0xFF2196F3),
                "Irritado" to Color(0xFFF44336),
                "Ansioso" to Color(0xFFFFC107)
            )
            val emojis = listOf(
                "😊" to "Feliz",
                "😐" to "Neutro",
                "😔" to "Triste",
                "😡" to "Irritado",
                "😰" to "Ansioso"
            )
            EmojiSelector(
                emojis = emojis,
                emojiColors = emojiColors,
                selectedMood = selectedMood,
                onSelected = { mood -> selectedMood = mood },
                modifier = Modifier.padding(bottom = 25.dp)
            )


            Text(
                "Sentimentos Envolvidos",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Text(
                "Selecione os sentimentos percebidos.",
                fontSize = 13.sp,
                color = Color(0xFF666666),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            MultiSelectDropdown(
                allOptions = feelings,
                selectedOptions = selectedFeelings
            )
            Spacer(modifier = Modifier.height(35.dp))


            Text(
                "Selecione o comportamento do paciente",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Text(
                "Escolha uma das opções abaixo.",
                fontSize = 13.sp,
                color = Color(0xFF666666),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            SingleSelectDropdown(
                modifier = Modifier.padding(bottom = 10.dp),
                options = behaviorOptions,
                selectedOption = selectedBehavior,
                onSelect = { selectedBehavior = it },
                placeholder = "Selecione o comportamento"
            )
            Spacer(modifier = Modifier.height(35.dp))


            Text(
                "Duração do comportamento do paciente",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Text(
                "Por quanto tempo o comportamento persistiu?",
                fontSize = 13.sp,
                color = Color(0xFF666666),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            SingleSelectDropdown(
                modifier = Modifier.padding(bottom = 10.dp),
                options = durationOptions,
                selectedOption = selectedDuration,
                onSelect = { selectedDuration = it },
                placeholder = "Selecione a duração"
            )
            Spacer(modifier = Modifier.height(35.dp))


            Text(
                "Selecione a intensidade do comportamento do paciente",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))


            val intensityColors = listOf(
                Color(0xFF4CAF50),
                Color(0xFFCDDC39),
                Color(0xFFFFEB3B),
                Color(0xFFFF9800),
                Color(0xFFF44336)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                (1..5).forEach { num ->
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .border(
                                width = 1.dp,
                                color = Color.LightGray,
                                shape = CircleShape
                            )
                            .background(
                                color = if (selectedIntensity == num) intensityColors[num - 1]
                                else Color.Transparent,
                                shape = CircleShape
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


            Text(
                "O que desencadeou o comportamento?",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Text(
                "Selecione o que pode ter causado esse comportamento.",
                fontSize = 13.sp,
                color = Color(0xFF666666),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            SingleSelectDropdown(
                modifier = Modifier.padding(bottom = 10.dp),
                options = triggerOptions,
                selectedOption = selectedTrigger,
                onSelect = { newValue ->
                    selectedTrigger = newValue
                },
                placeholder = "Selecione o gatilho"
            )
            Spacer(modifier = Modifier.height(35.dp))


            Text(
                "Data e Hora do Ocorrido",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Text(
                "Selecione a data e hora.",
                fontSize = 13.sp,
                color = Color(0xFF666666),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFCCCCCC), RoundedCornerShape(5.dp))
                    .clickable { showTimePicker = true }
                    .padding(16.dp)
            ) {
                Text(
                    text = selectedDateTime.format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                    )
                )
            }
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                "Observações adicionais",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Text(
                "Adicione notas sobre o comportamento do paciente.",
                fontSize = 13.sp,
                color = Color(0xFF666666),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            TextField(
                value = observations,
                onValueChange = { observations = it },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    focusedContainerColor = Color(0xFFF5F5F5)
                )
            )
            Spacer(modifier = Modifier.height(25.dp))

            if (showSuccessDialog) {
                AlertDialog(
                    onDismissRequest = {
                        viewModel.hideSuccessDialog()
                        if (isFromCalendarState) {
                            onNavigateToCalendar()
                        } else {
                            onNavigateToHome()
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.surface,
                    title = {
                        Text(
                            text = "Registro salvo!",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    text = {
                        Text(
                            text = "Seu registro foi salvo com sucesso.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.hideSuccessDialog()
                                if (isFromCalendarState) {
                                    onNavigateToCalendar()
                                } else {
                                    onNavigateToHome()
                                }
                            }
                        ) {
                            Text(
                                text = "OK",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    properties = DialogProperties(
                        dismissOnBackPress = true,
                        dismissOnClickOutside = true
                    )
                )
            }

            Button(
                onClick = {
                    viewModel.saveBehaviorRecord(
                        behaviorId = 1,
                        mood = selectedMood,
                        feelings = selectedFeelings.map { it.name },
                        intensity = selectedIntensity ?: 1,
                        duration = selectedDuration,
                        trigger = selectedTrigger,
                        notes = observations,
                        dateTime = selectedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(
                    "Registrar Humor",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(15.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                TextButton(onClick = {
                    if (isFromCalendarState) {
                        onNavigateToCalendar()
                    } else {
                        onNavigateToHome()
                    }
                }) {
                    Text("Cancelar")
                }
            }
        }
    }

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            properties = DialogProperties(dismissOnClickOutside = true),
            title = { Text("Selecione a hora") },
            text = {
                Column {
                    if (!isFromCalendarState) {
                        // TODO: Adicionar DatePicker, se desejado
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        NumberPicker(
                            value = selectedDateTime.hour,
                            range = 0..23,
                            onValueChange = { hour ->
                                selectedDateTime = selectedDateTime.withHour(hour)
                            }
                        )
                        Text(":")
                        NumberPicker(
                            value = selectedDateTime.minute,
                            range = 0..59,
                            onValueChange = { minute ->
                                selectedDateTime = selectedDateTime.withMinute(minute)
                            }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun EmojiSelector(
    emojis: List<Pair<String, String>>,
    emojiColors: Map<String, Color>,
    selectedMood: String?,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        emojis.forEach { (emoji, label) ->
            val color = emojiColors[label] ?: Color(0xFFF0F0F0)
            EmojiButton(
                emoji = emoji,
                label = label,
                isSelected = (selectedMood == label),
                color = color,
                onSelected = { onSelected(label) }
            )
        }
    }
}
@Composable
fun EmojiButton(
    emoji: String,
    label: String,
    isSelected: Boolean,
    color: Color,
    onSelected: () -> Unit
) {
    val backgroundColor = if (isSelected) color else Color(0xFFF0F0F0)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onSelected() }
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(backgroundColor, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = emoji, fontSize = 28.sp)
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF666666)
        )
    }
}


data class Feeling(val text: String, val color: Color)
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
        Box(modifier = Modifier.padding(8.dp)) {
            label()
        }
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
                    text = selectedOptions.joinToString(", ") { it.name },
                    color = Color.Black
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 300.dp)
        ) {
            allOptions.forEach { feeling ->
                DropdownMenuItem(
                    text = { Text(feeling.name) },
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
fun SingleSelectDropdown(
    modifier: Modifier = Modifier,
    options: List<String>,
    selectedOption: String,
    onSelect: (String) -> Unit,
    placeholder: String
) {
    var dropdownExpanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFCCCCCC), RoundedCornerShape(5.dp))
                .clickable {
                    focusManager.clearFocus()
                    dropdownExpanded = !dropdownExpanded
                }
                .padding(16.dp)
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
@Composable
private fun NumberPicker(
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
            onClick = {
                if (value < range.last) onValueChange(value + 1)
            }
        ) {
            Text("▲")
        }
        Text(
            text = String.format("%02d", value),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        IconButton(
            onClick = {
                if (value > range.first) onValueChange(value - 1)
            }
        ) {
            Text("▼")
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun AddScreenPreview() {
    MaterialTheme {
        AddScreen()
    }
}
