package com.example.neurotrack.ui.screens.settings

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import java.io.File
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    val darkMode by viewModel.darkMode.collectAsState()
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()
    var dataExportDialogVisible by remember { mutableStateOf(false) }
    var aboutDialogVisible by remember { mutableStateOf(false) }
    
    // Launcher para salvar o arquivo PDF
    val savePdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                state.convertedPdfPath?.let { path ->
                    val pdfFile = File(path)
                    val success = viewModel.saveFileToUri(context, pdfFile, uri)
                    if (success) {
                        Toast.makeText(context, "Arquivo salvo com sucesso!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Erro ao salvar arquivo", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Configurações") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Perfil
            SettingsSection(title = "") {
                SettingsCard(
                    icon = Icons.Default.Person,
                    title = "João Silva",
                    subtitle = "Editar perfil",
                    onClick = { /* Implementar edição de perfil */ }
                )
            }
            
            // Aparência
            SettingsSection(title = "Aparência") {
                SettingsSwitchCard(
                    icon = Icons.Default.DarkMode,
                    title = "Modo escuro",
                    subtitle = "Ativar tema escuro no aplicativo",
                    checked = darkMode,
                    onCheckedChange = { viewModel.toggleDarkMode() }
                )
            }
            
            // Notificações
            SettingsSection(title = "Notificações") {
                SettingsSwitchCard(
                    icon = Icons.Default.Notifications,
                    title = "Lembretes diários",
                    subtitle = "Receber lembretes para registrar comportamentos",
                    checked = notificationsEnabled,
                    onCheckedChange = { viewModel.toggleNotifications() }
                )
            }
            
            // Dados
            SettingsSection(title = "Dados") {
                SettingsCard(
                    icon = Icons.Default.CloudDownload,
                    title = "Exportar dados",
                    subtitle = "Exportar registros para arquivo CSV",
                    onClick = { viewModel.exportDataToCsv(context) }
                )
                
                SettingsCard(
                    icon = Icons.Default.Delete,
                    title = "Limpar dados",
                    subtitle = "Apagar todos os registros",
                    onClick = { /* Implementar limpeza de dados */ }
                )
            }
            
            // Sobre
            SettingsSection(title = "Sobre") {
                SettingsCard(
                    icon = Icons.Default.Info,
                    title = "Sobre o NeuroTracker",
                    subtitle = "Versão 1.0.0",
                    onClick = { aboutDialogVisible = true }
                )
            }
        }
    }
    
    // Diálogos
    if (state.showShareDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissShareDialog() },
            title = { Text("Exportação concluída") },
            text = { 
                Text("Os dados foram exportados com sucesso. Deseja compartilhar o arquivo?") 
            },
            confirmButton = {
                TextButton(
                    onClick = { 
                        viewModel.shareExportedFile(context)
                        viewModel.dismissShareDialog()
                    }
                ) {
                    Text("Compartilhar")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissShareDialog() }) {
                    Text("Fechar")
                }
            }
        )
    }
    
    if (state.showConversionErrorDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissErrorDialog() },
            title = { Text("Erro na exportação") },
            text = { 
                Text(state.conversionError ?: "Ocorreu um erro ao exportar os dados.") 
            },
            confirmButton = {
                TextButton(onClick = { viewModel.dismissErrorDialog() }) {
                    Text("OK")
                }
            }
        )
    }
    
    // Diálogo sobre o app
    if (aboutDialogVisible) {
        AlertDialog(
            onDismissRequest = { aboutDialogVisible = false },
            title = { Text("Sobre o NeuroTracker") },
            text = {
                Column {
                    Text("NeuroTracker v1.0.0")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Um aplicativo para rastreamento e análise de comportamentos neurodivergentes.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("© 2023 NeuroTracker")
                }
            },
            confirmButton = {
                TextButton(onClick = { aboutDialogVisible = false }) {
                    Text("Fechar")
                }
            }
        )
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        if (title.isNotEmpty()) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        content()
    }
}

@Composable
fun SettingsCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SettingsSwitchCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
} 